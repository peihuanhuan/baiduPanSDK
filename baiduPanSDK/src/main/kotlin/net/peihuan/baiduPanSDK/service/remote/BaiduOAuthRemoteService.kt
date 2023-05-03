package net.peihuan.baiduPanSDK.service.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import mu.KotlinLogging
import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.domain.dto.AuthorizeResponseDTO
import net.peihuan.baiduPanSDK.exception.BaiduPanException
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request


class BaiduOAuthRemoteService(
    private val okHttpClient: OkHttpClient,
    private val baiduPanProperties: BaiduPanProperties
) {

    private val gson =  Gson()

    private val BASE_URL = "http://openapi.baidu.com"

    fun authorize(redirectUrl: String, state: String? = null): String {

        val url = "${BASE_URL}/oauth/2.0/authorize".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("response_type", "code")
            .addQueryParameter("client_id", baiduPanProperties.clientId)
            .addQueryParameter("redirect_uri", redirectUrl)
            .addQueryParameter("scope", "basic,netdisk")
            .addQueryParameter("state", state)
            .addQueryParameter("device_id", baiduPanProperties.deviceId)
            .build()

        return url.toString()
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

        val json = response.body?.string()
        return gson.fromJson(json, AuthorizeResponseDTO::class.java)

    }

    fun refreshToken(refreshToken: String): AuthorizeResponseDTO {
        val url = "${BASE_URL}/oauth/2.0/token".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("client_id", baiduPanProperties.clientId)
            .addQueryParameter("refresh_token", refreshToken)
            .addQueryParameter("client_secret", baiduPanProperties.clientSecret)
            .addQueryParameter("grant_type", "refresh_token")
            .build()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()

        val json = response.body?.string()
        val obj = gson.fromJson(json, JsonObject::class.java)
        if(obj.has("error")) {
            throw BaiduPanException("刷新 accessToken 失败 $json")
        }
        return gson.fromJson(json, AuthorizeResponseDTO::class.java)

    }


}