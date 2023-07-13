package net.peihuan.baiduPanSDK.service

import java.util.concurrent.locks.Lock

interface BaiduOAuthConfigStorage {

    fun getRefreshToken(userId: String): String?

    fun setRefreshToken(userId: String, refreshToken: String, expiresInSeconds: Int)

    fun getAccessToken(userId: String): String?

    fun getAccessTokenLock(userId: String): Lock

    fun isAccessTokenExpired(userId: String): Boolean

    fun expireAccessToken(userId: String)

    fun expireRefreshToken(userId: String)

    fun updateAccessToken(userId: String, accessToken: String, expiresInSeconds: Int)


}