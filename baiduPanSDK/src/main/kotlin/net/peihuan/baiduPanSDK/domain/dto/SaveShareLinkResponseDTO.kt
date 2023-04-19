package net.peihuan.baiduPanSDK.domain.dto

data class SaveShareLinkResponseDTO(
        val errno : Int,
        val newno : Int,

        val request_id: String,
        val show_msg: String,
        val taskid: Long,
        val info: List<Info>,
        val extra: Extra

)

data class Info(
        val errno:Int,
        val path:String,
        val fsid:String,
)
data class Extra(
        val list: List<ExtraList>
)
data class ExtraList(
        val from_fs_id:Long,
        val from:String,
        val to:String,
)