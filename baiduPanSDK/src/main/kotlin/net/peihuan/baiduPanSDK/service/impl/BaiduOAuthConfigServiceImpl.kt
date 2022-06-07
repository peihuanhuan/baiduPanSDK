package net.peihuan.baiduPanSDK.service.impl

import net.peihuan.baiduPanSDK.service.BaiduOAuthConfigStorage
import net.peihuan.baiduPanSDK.service.BaiduOps
import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

class BaiduOAuthConfigServiceImpl(
    private val baiduOps: BaiduOps) : BaiduOAuthConfigStorage {


    private fun getAccessTokenKey(userId: String) = "baidu_pan:access_token:$userId"
    private fun getRefreshTokenKey(userId: String) = "baidu_pan:refresh_token:$userId"

    private fun getLockKey(userId: String) = "baidu_pan:lock:$userId"


    override fun getRefreshToken(userId: String): String? {
        return baiduOps.getValue(getRefreshTokenKey(userId))
    }

    override fun setRefreshToken(userId: String, refreshToken: String, expiresInSeconds: Int) {
        baiduOps.setValue(getRefreshTokenKey(userId), refreshToken, expiresInSeconds, TimeUnit.SECONDS)
    }

    override fun getAccessToken(userId: String): String? {
        return baiduOps.getValue(getAccessTokenKey(userId))
    }

    override fun getAccessTokenLock(userId: String): Lock {
        return baiduOps.getLock(getLockKey(userId))
    }

    override fun isAccessTokenExpired(userId: String): Boolean {
        return (baiduOps.getExpire(getAccessTokenKey(userId)) ?: 0) < 2
    }

    override fun expireAccessToken(userId: String) {
        baiduOps.expire(getAccessTokenKey(userId), 0, TimeUnit.SECONDS)
    }

    override fun updateAccessToken(userId: String, accessToken: String, expiresInSeconds: Int) {
        baiduOps.setValue(getAccessTokenKey(userId), accessToken, expiresInSeconds, TimeUnit.SECONDS)

    }


}