package net.peihuan.baiduPanSDK.service.remote

import com.google.gson.Gson
import mu.KotlinLogging
import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.feign.dto.AuthorizeResponseDTO
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request


class BaiduOAuthRemoteService(
    private val okHttpClient: OkHttpClient,
    private val baiduPanProperties: BaiduPanProperties
) {

    private val log = KotlinLogging.logger {}
    private val gson =  Gson()

    final val BASE_URL = "http://openapi.baidu.com"

    fun authorize(redirectUrl: String): String {

        val url = "${BASE_URL}/oauth/2.0/authorize".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("response_type", "code")
            .addQueryParameter("client_id", baiduPanProperties.clientId)
            .addQueryParameter("redirect_uri", redirectUrl)
            .addQueryParameter("device_id", baiduPanProperties.deviceId)
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        return response.body.toString()
    }

    fun getToken(code: String, redirectUrl: String): AuthorizeResponseDTO {
        val url = "${BASE_URL}/oauth/2.0/token".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("code", code)
            .addQueryParameter("client_id", baiduPanProperties.clientId)
            .addQueryParameter("redirect_uri", redirectUrl)
            .addQueryParameter("client_secret", baiduPanProperties.clientSecret)
            .addQueryParameter("grant_type", "authorization_code")
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body.toString()
        return gson.fromJson(json, AuthorizeResponseDTO::class.java)

    }


}