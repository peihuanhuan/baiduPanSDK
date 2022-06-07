package net.peihuan.baiduPanSDK.domain.dto

data class ShareResponseDTO(
        val errno : Int,
        val request_id: String,
        val shareid: Long,
        val link: String,
        val expiredType:Int,
        val shorturl: String,
        val pwd: String,
        val uk : Long,

)
