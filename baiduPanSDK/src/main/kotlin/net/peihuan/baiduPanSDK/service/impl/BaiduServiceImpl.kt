package net.peihuan.baiduPanSDK.service.impl

import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.domain.dto.AuthorizeResponseDTO
import net.peihuan.baiduPanSDK.exception.BaiduPanException
import net.peihuan.baiduPanSDK.service.BaiduOAuthConfigStorage
import net.peihuan.baiduPanSDK.service.BaiduService
import net.peihuan.baiduPanSDK.service.PanService
import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
import net.peihuan.baiduPanSDK.service.remote.BaiduPanRemoteService
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class BaiduServiceImpl(
    baiduPanProperties: BaiduPanProperties,
    private var okHttpClient: OkHttpClient
) : BaiduService {

    private val baiduOAuthRemoteService = BaiduOAuthRemoteService(okHttpClient, baiduPanProperties)
    private val panService: PanService = PanServiceImpl(this, baiduPanProperties)
    private var configStorage: BaiduOAuthConfigStorage = BaiduDefaultConfigImpl()
    private val refreshTokenExpireSecond = TimeUnit.DAYS.toSeconds(1).toInt() * 3650

    override fun getConfigStorage(): BaiduOAuthConfigStorage {
        return configStorage
    }

    override fun setConfigStorage(storage: BaiduOAuthConfigStorage) {
        configStorage = storage
    }


    override fun getPanService(): PanService {
        return panService
    }

    override fun getOkHttpClient(): OkHttpClient {
        return okHttpClient
    }

    override fun setOkHttpClient(okHttpClient: OkHttpClient) {
        this.okHttpClient = okHttpClient
    }


    override fun getAuthorizeUrl(redirectUrl: String, state:String?): String {
        return baiduOAuthRemoteService.authorize(redirectUrl, state)
    }

    override fun getTokenByCode(userId: String, code: String, redirectUrl: String): String {
        val token = baiduOAuthRemoteService.getToken(code, redirectUrl)
        configStorage.updateAccessToken(userId, token.access_token, token.expires_in)
        // 刷新 token 有效期 十年
        configStorage.setRefreshToken(userId, token.refresh_token, refreshTokenExpireSecond)
        return token.access_token
    }

    override fun getAccessToken(userId: String): String {
        val accessTokenExpired = configStorage.isAccessTokenExpired(userId)
        if (!accessTokenExpired) {
            return configStorage.getAccessToken(userId)!!
        }

        val lock = configStorage.getAccessTokenLock(userId)
        lock.lock()
        try {
            val refreshToken =
                configStorage.getRefreshToken(userId) ?: throw BaiduPanException("$userId 的 refreshToken 为空")
            val refreshResp = baiduOAuthRemoteService.refreshToken(refreshToken)
            configStorage.updateAccessToken(userId, refreshResp.access_token, refreshResp.expires_in)
            configStorage.setRefreshToken(userId, refreshResp.refresh_token, refreshTokenExpireSecond)
            return refreshResp.access_token
        } finally {
            lock.unlock()
        }
    }




}