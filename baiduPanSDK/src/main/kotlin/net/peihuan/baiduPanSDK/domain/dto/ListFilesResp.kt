package net.peihuan.baiduPanSDK.domain.dto

data class ListFilesResp(
    val errno: Int?,
    val guid: Int?,
    val list: List<Filemeta>?,
    val guid_info: String?,
    val request_id: Long?
)