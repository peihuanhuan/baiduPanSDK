package net.peihuan.baiduPanSDK.domain.dto

data class VerifyShareLinkResponseDTO(
        val errno : Int,

        val request_id: String,
        val err_msg: String,
        val randsk: String,
)
