package net.peihuan.baiduPanSDK.domain.dto

data class PrecreateResponseDTO(
        val errno : Int,
        val path: String,
        val uploadid: String,
        val return_type : Int,
        val block_list: List<Int>

)
