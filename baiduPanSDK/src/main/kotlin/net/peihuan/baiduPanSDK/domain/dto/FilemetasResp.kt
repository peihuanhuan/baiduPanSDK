package net.peihuan.baiduPanSDK.domain.dto

data class FilemetasResp(
    val errmsg: String?,
    val errno: Int?,
    val list: List<Filemeta>?,
    val names: Names?,
    val request_id: String?
)