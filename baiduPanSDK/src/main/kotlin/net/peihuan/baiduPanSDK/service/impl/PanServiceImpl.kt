package net.peihuan.baiduPanSDK.service.impl

import com.google.gson.Gson
import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.domain.constant.AsyncModel
import net.peihuan.baiduPanSDK.domain.constant.ManageFileOpera
import net.peihuan.baiduPanSDK.domain.constant.SaveShareAsyncModel
import net.peihuan.baiduPanSDK.domain.dto.*
import net.peihuan.baiduPanSDK.exception.BaiduPanException
import net.peihuan.baiduPanSDK.service.BaiduService
import net.peihuan.baiduPanSDK.service.PanService
import net.peihuan.baiduPanSDK.service.remote.BaiduPanRemoteService
import net.peihuan.baiduPanSDK.util.md5
import java.io.File
import java.io.FileInputStream

class PanServiceImpl(
    private val baiduService: BaiduService,
    private val baiduPanProperties: BaiduPanProperties,
) : PanService {


    private val gson = Gson()


    private val baiduPanRemoteService = BaiduPanRemoteService(baiduService.getOkHttpClient(), baiduPanProperties)


    private val part_max_size: Int = 4 * 1024 * 1024


    override fun managerFile(userId: String, opera: ManageFileOpera, asyncModel: AsyncModel, fileList: List<Any>, ondup :String) : Boolean {
        var finalList = fileList
        if (opera == ManageFileOpera.DELETE) {
            finalList = fileList.map { baiduPanProperties.rootDir.removeSuffix("/") + "/" + it.toString().removePrefix("/") }
        }
        // todo  copy rename


        val accessToken = baiduService.getAccessToken(userId)

        val response = baiduPanRemoteService.manageFile(accessToken, opera, asyncModel, finalList,ondup)
        return response.errno == 0
    }

    override fun shareFiles(userId: String, fids: List<Long>, period: Int, desc: String): ShareResponseDTO {
        if (baiduPanProperties.shareThirdId == null || baiduPanProperties.shareSecret.isNullOrBlank()) {
            throw BaiduPanException("没有配置 shareThirdId 或 shareSecret，无法分享")
        }
        val accessToken = baiduService.getAccessToken(userId)

        val schannel = 4
        val sign =
            "${baiduPanProperties.shareThirdId}${gson.toJson(fids)}$schannel${baiduPanProperties.shareSecret}".md5()

        return baiduPanRemoteService.share(
            accessToken = accessToken, schannel = schannel, third_type = baiduPanProperties.shareThirdId!!,
            period = period, fid_list = fids, csign = sign, description = desc
        )
    }

    override fun saveShareLink(userId: String, savePath: String, fs_ids: List<Long>, share: ShareResponseDTO, async: SaveShareAsyncModel) : SaveShareLinkResponseDTO {
        var encodePath = baiduPanProperties.rootDir.removeSuffix("/") + "/" + savePath.removePrefix("/")
        encodePath = encodePath.removeSuffix("/")

        if (baiduPanProperties.shareThirdId == null || baiduPanProperties.shareSecret.isNullOrBlank()) {
            throw BaiduPanException("没有配置 shareThirdId 或 shareSecret，无法分享")
        }

        val accessToken = baiduService.getAccessToken(userId)

        val verifyResp = baiduPanRemoteService.verifyShareLink(accessToken = accessToken, shareid = share.shareid, pwd = share.pwd, uk = share.uk, redirect = 0, third_type = baiduPanProperties.shareThirdId!!)

        var finalFids = fs_ids
        if (finalFids.isEmpty()) {
            val shareFiles = baiduPanRemoteService.getShareFiles(accessToken = accessToken,
                    shareid = share.shareid,
                    uk = share.uk,
                    page = 1,
                    num = 100,
                    sekey = verifyResp.randsk
            )
            finalFids = shareFiles.list.map { it.fs_id }
        }

        return baiduPanRemoteService.saveShareLink(accessToken = accessToken,
                shareid = share.shareid,
                sekey = verifyResp.randsk,
                async = async.code,
                from = share.uk,
                path = encodePath,
                fsidlist = finalFids)

    }

    override fun filemetas(userId: String, fsids: List<Long>, path: String?, dlink: Int): List<Filemeta> {
        val accessToken = baiduService.getAccessToken(userId)
        val resp =
            baiduPanRemoteService.filemetas(accessToken = accessToken, fid_list = fsids, path = path, dlink = dlink)
        return resp.list ?: emptyList()
    }

    override fun listFiles(userId: String, dir: String, order: String, desc: Int, start: Int, limit: Int, folder: Int, ): List<Filemeta> {
        val accessToken = baiduService.getAccessToken(userId)
        val listFiles = baiduPanRemoteService.listFiles(
            accessToken = accessToken,
            order = order,
            desc = desc,
            start = start,
            limit = limit,
            folder = folder,
            dir = baiduPanProperties.rootDir.removeSuffix("/") + "/" + dir.removePrefix("/")
        )
        return listFiles
    }

    override fun uploadFile(userId: String, path: String, file: File, rtype: RtypeEnum): CreateResponseDTO {
        val encodePath = baiduPanProperties.rootDir.removeSuffix("/") + "/" + path.removePrefix("/")
        val accessToken = baiduService.getAccessToken(userId)
        val blockList = getBlockList(file)
        val precreate = baiduPanRemoteService.precreate(
                accessToken = accessToken,
                path = encodePath,
                size = file.length(),
                isdir = false,
                block_list = blockList,
                rtype = rtype
        )
        if (precreate.uploadid == null) {
            throw BaiduPanException("上传失败 errno" + precreate.errno)
        }

        upload(file, accessToken, encodePath, precreate)

        val createResponse = baiduPanRemoteService.create(
                accessToken = accessToken,
                uploadid = precreate.uploadid,
                path = encodePath,
                isdir = false,
                size = file.length(),
                block_list = blockList,
                rtype = rtype
        )
        if (createResponse.fs_id == 0L) {
            throw BaiduPanException("文件合并失败，错误码 " + createResponse.errno)
        }
        return createResponse
    }

    override fun createDir(userId: String, path: String, rtype: RtypeEnum) {
        val encodePath = baiduPanProperties.rootDir.removeSuffix("/") + "/" + path.removePrefix("/")
        val accessToken = baiduService.getAccessToken(userId)
        val createResponse = baiduPanRemoteService.create(
                accessToken = accessToken,
                uploadid = null,
                path = encodePath,
                isdir = true,
                size = null,
                block_list = null,
                rtype = rtype
        )
        if (createResponse.errno == -8) {
            // 文件或目录已存在
            return
        }
        if (createResponse.fs_id == 0L) {
            throw BaiduPanException("创建文件夹失败，错误码 " + createResponse.errno)
        }
    }

    private fun upload(
        file: File,
        accessToken: String,
        path: String,
        precreate: PrecreateResponseDTO
    ) {
        var partseq = 0
        val fileInputStream = FileInputStream(file)
        val buf = ByteArray(part_max_size)
        var length: Int
        while (fileInputStream.read(buf).also { length = it } != -1) {
            baiduPanRemoteService.superfile2(
                accessToken = accessToken,
                path = path,
                uploadid = precreate.uploadid!!,
                partseq = partseq++,
                file = buf.copyOfRange(0, length)
            )
        }
    }


    private fun getBlockList(file: File): MutableList<String> {
        val blockList = mutableListOf<String>()

        val fileInputStream = FileInputStream(file)
        val buf = ByteArray(part_max_size)
        var length: Int
        while (fileInputStream.read(buf).also { length = it } != -1) {
            blockList.add(buf.copyOfRange(0, length).md5())
        }
        fileInputStream.close()
        return blockList
    }


}