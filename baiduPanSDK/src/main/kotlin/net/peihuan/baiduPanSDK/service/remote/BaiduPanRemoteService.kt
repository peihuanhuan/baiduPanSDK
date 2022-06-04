package net.peihuan.baiduPanSDK.service.remote

import com.google.gson.Gson
import mu.KotlinLogging
import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.feign.dto.CreateResponseDTO
import net.peihuan.baiduPanSDK.feign.dto.PrecreateResponseDTO
import net.peihuan.baiduPanSDK.feign.dto.RtypeEnum
import net.peihuan.baiduPanSDK.feign.dto.UploadResponseDTO
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File


class BaiduPanRemoteService(
    private val okHttpClient: OkHttpClient,
    private val baiduPanProperties: BaiduPanProperties
) {

    private val log = KotlinLogging.logger {}
    private val gson =  Gson()

    val BASE_URL = "http://pan.baidu.com"


    fun precreate(accessToken: String, path: String, size:Int, isdir:Boolean, block_list: List<String>, rtype: RtypeEnum = RtypeEnum.RENAME): PrecreateResponseDTO {

        val requestBody: RequestBody = FormBody.Builder()
            .add("method", "precreate")
            .add("access_token", accessToken)
            .add("path", path)
            .add("size", size.toString())
            .add("isdir", if (isdir) "1" else "0")
            .add("block_list", block_list.toString())
            .add("autoinit", "1")
            .add("rtype", rtype.code)
            .build()
        val request = Request.Builder()
            .url("${BASE_URL}/rest/2.0/xpan/file")
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, PrecreateResponseDTO::class.java)

    }

    fun superfile2(accessToken: String, path: String, uploadid: String, partseq:Int, file: ByteArray): UploadResponseDTO {

        val url = "${BASE_URL}/rest/2.0/pcs/superfile2".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "upload")
            .addQueryParameter("access_token", accessToken)
            .addQueryParameter("path", path)
            .addQueryParameter("type", "tmpfile")
            .addQueryParameter("uploadid", uploadid)
            .addQueryParameter("partseq", partseq.toString())
            .build()


        val x: RequestBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)
        val requestBody: RequestBody = MultipartBody.Builder()
            .addFormDataPart("file", "file", x)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, UploadResponseDTO::class.java)

    }


    fun create(accessToken: String, path: String, size:Int, isdir:Boolean, block_list: List<String>, rtype: RtypeEnum = RtypeEnum.RENAME): CreateResponseDTO {

        val requestBody: RequestBody = FormBody.Builder()
            .add("method", "create")
            .add("access_token", accessToken)
            .add("path", path)
            .add("size", size.toString())
            .add("isdir", if (isdir) "1" else "0")
            .add("block_list", block_list.toString())
            .add("autoinit", "1")
            .add("rtype", rtype.code)
            .build()
        val request = Request.Builder()
            .url("${BASE_URL}/rest/2.0/xpan/file")
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, CreateResponseDTO::class.java)

    }

}