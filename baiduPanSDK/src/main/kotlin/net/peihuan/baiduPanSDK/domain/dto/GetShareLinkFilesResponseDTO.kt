package net.peihuan.baiduPanSDK.domain.dto

data class GetShareLinkFilesResponseDTO(
        val errno : Int,

        val request_id: String,
        val server_time: String,
        val err_msg: String,
        val title: String,
        val cfrom_id: String,
        val list: List<Filemeta>,
)
