package net.peihuan.baiduPanSDK.service

import okhttp3.OkHttpClient

interface BaiduService {
    fun getAuthorizeUrl(redirectUrl: String, state:String?=null): String

    fun getAccessToken(userId: String): String

    fun getAccessToken(userId: String, code: String, redirectUrl: String): String

    fun setConfigStorage(storage: BaiduOAuthConfigStorage)

    fun getConfigStorage(): BaiduOAuthConfigStorage

    fun getPanService(): PanService

    fun getOkHttpClient(): OkHttpClient

    fun setOkHttpClient(okHttpClient: OkHttpClient)
}