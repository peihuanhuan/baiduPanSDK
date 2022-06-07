package net.peihuan.baiduPanSDK.service.remote

import com.google.gson.Gson
import mu.KotlinLogging
import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.domain.dto.CreateResponseDTO
import net.peihuan.baiduPanSDK.domain.dto.PrecreateResponseDTO
import net.peihuan.baiduPanSDK.domain.dto.RtypeEnum
import net.peihuan.baiduPanSDK.domain.dto.UploadResponseDTO
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class BaiduPanRemoteService(
    private val okHttpClient: OkHttpClient,
) {

    private val gson =  Gson()

    private val BASE_URL = "http://pan.baidu.com"


    fun precreate(accessToken: String, path: String, size:Long, isdir:Boolean, block_list: List<String>, rtype: RtypeEnum = RtypeEnum.RENAME): PrecreateResponseDTO {

        val url = "${BASE_URL}/rest/2.0/xpan/file".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "precreate")
            .addQueryParameter("access_token", accessToken)
            .build()

        val requestBody: RequestBody = FormBody.Builder()
            .add("path", path)
            .add("size", size.toString())
            .add("isdir", if (isdir) "1" else "0")
            .add("block_list", gson.toJson(block_list))
            .add("autoinit", "1")
            .add("rtype", rtype.code)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, PrecreateResponseDTO::class.java)

    }

    fun superfile2(accessToken: String, path: String, uploadid: String, partseq:Int, file: ByteArray): UploadResponseDTO {

        val url = "https://d.pcs.baidu.com/rest/2.0/pcs/superfile2".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "upload")
            .addQueryParameter("access_token", accessToken)
            .addQueryParameter("path", path)
            .addQueryParameter("type", "tmpfile")
            .addQueryParameter("uploadid", uploadid)
            .addQueryParameter("partseq", partseq.toString())
            .build()


        val requestBody: RequestBody = RequestBody.create("text/plain;charset=utf-8".toMediaType(), file)
        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, UploadResponseDTO::class.java)

    }


    fun create(accessToken: String, path: String, size:Long, uploadid: String, isdir:Boolean, block_list: List<String>, rtype: RtypeEnum = RtypeEnum.RENAME): CreateResponseDTO {

        val url = "${BASE_URL}/rest/2.0/xpan/file".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "create")
            .addQueryParameter("access_token", accessToken)
            .build()

        val requestBody: RequestBody = FormBody.Builder()
            .add("path", path)
            .add("size", size.toString())
            .add("isdir", if (isdir) "1" else "0")
            .add("block_list", gson.toJson(block_list))
            .add("uploadid", uploadid)
            .add("autoinit", "1")
            .add("rtype", rtype.code)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, CreateResponseDTO::class.java)

    }

}