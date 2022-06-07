package net.peihuan.baiduPanSDK.service

import net.peihuan.baiduPanSDK.domain.dto.AuthorizeResponseDTO

interface BaiduService {
    fun getAuthorizeUrl(redirectUrl: String): String

    fun getAccessToken(userId: String): String

    fun getTokenByCode(userId: String, code: String, redirectUrl: String): String

    fun setConfigStorage(storage: BaiduOAuthConfigStorage)

    fun getPanService(): PanService
}