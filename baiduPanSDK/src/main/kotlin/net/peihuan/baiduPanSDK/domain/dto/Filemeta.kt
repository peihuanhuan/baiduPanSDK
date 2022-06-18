package net.peihuan.baiduPanSDK.domain.dto

data class Filemeta(
    val category: Int,
    val date_taken: Int,
    val dlink: String?,
    val filename: String,
    val fs_id: Long,
    val height: Int?,
    val isdir: Int,
    val md5: String,
    val oper_id: Long,
    val path: String,
    val server_ctime: Int,
    val server_mtime: Int,
    val size: Int,
    val thumbs: Thumbs?,
    val width: Int?,


    val dir_empty: Int?,
    val empty: Int?,
    val local_ctime: Int?,
    val local_mtime: Int?,
    val privacy: Int?,
    val server_atime: Int?,
    val server_filename: String?,
    val share: Int?,
    val unlist: Int?
)