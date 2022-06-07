package net.peihuan.baiduPanSDK.util

import java.math.BigInteger
import java.security.MessageDigest


fun ByteArray.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this)).toString(16).padStart(32, '0')
}

fun String.md5(): String {
    return this.toByteArray().md5()
}