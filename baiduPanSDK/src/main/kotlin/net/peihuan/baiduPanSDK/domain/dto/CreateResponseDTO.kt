package net.peihuan.baiduPanSDK.domain.dto

data class CreateResponseDTO(
        val errno : Int,
        val fs_id: String,
        val md5: String,
        val server_filename: String,
        val category:Int,
        val path: String,
        val size: Long,
        val ctime : Long,
        val mtime: Long,
        val isdir: Int

)
