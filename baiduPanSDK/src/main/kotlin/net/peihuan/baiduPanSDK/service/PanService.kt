package net.peihuan.baiduPanSDK.service

import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.feign.dto.PrecreateResponseDTO
import net.peihuan.baiduPanSDK.service.remote.BaiduPanRemoteService
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream
import java.math.BigInteger
import java.net.URLEncoder
import java.security.MessageDigest

class PanService(
    private val baiduPanRemoteService: BaiduPanRemoteService,
    private val baiduOauthService: BaiduOauthService,
    private val baiduPanProperties: BaiduPanProperties,
) {

    private val part_max_size: Int = 4 * 1024 * 1024


    fun md5(input: ByteArray): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input)).toString(16).padStart(32, '0')
    }

    fun uploadFile(path: String, file: File) {
        val encodePath = baiduPanProperties.rootDir+URLEncoder.encode(path)
        val accessToken = baiduOauthService.getAccessToken()
        val blockList = getBlockList(file)
        val precreate = baiduPanRemoteService.precreate(
            accessToken = accessToken,
            path = encodePath,
            size = file.length(),
            isdir = false,
            block_list = blockList
        )

        upload(file, accessToken, encodePath, precreate)

        val x = baiduPanRemoteService.create(accessToken = accessToken, uploadid = precreate.uploadid, path = encodePath, isdir = false, size = file.length(), block_list = blockList)
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
            blockList.add(md5(buf.copyOfRange(0, length)))
        }
        fileInputStream.close()
        return blockList
    }


}