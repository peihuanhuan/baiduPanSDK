package net.peihuan.baiduPanSDK.service

import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

/**
 * 百度持久化相关操作，可用 mysql、redis、内存 自定义实现
 */
interface BaiduOps {
    fun getValue(key: String): String?

    fun setValue(key: String, value: String, expire: Int, timeUnit: TimeUnit)

    fun getExpire(key: String): Long?

    fun expire(key: String, expire: Int, timeUnit: TimeUnit)

    fun getLock(key: String): Lock

}