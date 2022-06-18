package net.peihuan.baiduPanSDK.service.impl

import com.google.gson.Gson
import net.peihuan.baiduPanSDK.config.BaiduPanProperties
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


    private val baiduPanRemoteService = BaiduPanRemoteService(baiduService.getOkHttpClient())


    private val part_max_size: Int = 4 * 1024 * 1024

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
            dir = dir
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