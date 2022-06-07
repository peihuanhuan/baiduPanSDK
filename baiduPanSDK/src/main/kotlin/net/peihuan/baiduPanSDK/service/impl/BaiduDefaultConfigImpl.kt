package net.peihuan.baiduPanSDK.service.impl

import net.peihuan.baiduPanSDK.service.BaiduOAuthConfigStorage
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * 基于内存的配置，生产环境需要持久化，不可使用
 */
class BaiduDefaultConfigImpl : BaiduOAuthConfigStorage {

    private val accessTokenMap = mutableMapOf<String, String>()
    private val refreshTokenMap = mutableMapOf<String, String>()

    private var accessTokenExpiresTimeMap = mutableMapOf<String, Long>()
    private var refreshTokenExpiresTimeMap = mutableMapOf<String, Long>()

    @Volatile
    private var accessTokenLock: Lock = ReentrantLock()

    override fun getRefreshToken(userId: String): String? {
        return refreshTokenMap.get(userId)
    }

    override fun setRefreshToken(userId: String, refreshToken: String, expiresInSeconds: Int) {
        this.refreshTokenMap.put(userId, refreshToken)
        this.refreshTokenExpiresTimeMap.put(userId, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L)
    }


    override fun getAccessToken(userId: String): String? {
        return this.accessTokenMap.get(userId)
    }

    override fun getAccessTokenLock(userId: String): Lock {
        return this.accessTokenLock
    }

    override fun isAccessTokenExpired(userId: String): Boolean {
        return System.currentTimeMillis() > accessTokenExpiresTimeMap.getOrDefault(userId, 0)
    }

    override fun expireAccessToken(userId: String) {
        accessTokenExpiresTimeMap.put(userId, 0)
    }

    override fun updateAccessToken(userId: String, accessToken: String, expiresInSeconds: Int) {
        accessTokenMap.put(userId, accessToken)
        // 预留200秒的时间
        accessTokenExpiresTimeMap.put(userId, System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L)

    }

}