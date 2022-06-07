package net.peihuan.baiduPanSDK.service

import net.peihuan.baiduPanSDK.domain.dto.AuthorizeResponseDTO
import okhttp3.OkHttpClient

interface BaiduService {
    fun getAuthorizeUrl(redirectUrl: String): String

    fun getAccessToken(userId: String): String

    fun getTokenByCode(userId: String, code: String, redirectUrl: String): String

    fun setConfigStorage(storage: BaiduOAuthConfigStorage)

    fun getConfigStorage(): BaiduOAuthConfigStorage

    fun getPanService(): PanService

    fun getOkHttpClient(): OkHttpClient

    fun setOkHttpClient(okHttpClient: OkHttpClient)
}