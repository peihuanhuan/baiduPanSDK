package net.peihuan.baiduPanSDK.service.impl

import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.exception.BaiduPanException
import net.peihuan.baiduPanSDK.domain.dto.CreateResponseDTO
import net.peihuan.baiduPanSDK.domain.dto.PrecreateResponseDTO
import net.peihuan.baiduPanSDK.service.BaiduService
import net.peihuan.baiduPanSDK.service.PanService
import net.peihuan.baiduPanSDK.service.remote.BaiduPanRemoteService
import net.peihuan.baiduPanSDK.util.md5
import java.io.File
import java.io.FileInputStream

class PanServiceImpl(
    private val baiduPanRemoteService: BaiduPanRemoteService,
    private val baiduService: BaiduService,
    private val baiduPanProperties: BaiduPanProperties,
) : PanService {

    private val part_max_size: Int = 4 * 1024 * 1024

    override fun uploadFile(userId: String, path: String, file: File): CreateResponseDTO {
        val encodePath = baiduPanProperties.rootDir + path
        val accessToken = baiduService.getAccessToken(userId)
        val blockList = getBlockList(file)
        val precreate = baiduPanRemoteService.precreate(
            accessToken = accessToken,
            path = encodePath,
            size = file.length(),
            isdir = false,
            block_list = blockList
        )

        upload(file, accessToken, encodePath, precreate)

        val createResponse = baiduPanRemoteService.create(
            accessToken = accessToken,
            uploadid = precreate.uploadid,
            path = encodePath,
            isdir = false,
            size = file.length(),
            block_list = blockList
        )
        if (createResponse.fs_id.isBlank()) {
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
                uploadid = precreate.uploadid,
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