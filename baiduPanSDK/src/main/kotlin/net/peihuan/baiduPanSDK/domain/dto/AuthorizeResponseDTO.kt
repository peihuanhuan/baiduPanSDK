package net.peihuan.baiduPanSDK.domain.dto

data class AuthorizeResponseDTO(
        val expires_in : Int,
        val refresh_token : String,
        val access_token : String,
        val scope : String,
        val session_secret : String,
        val session_key : String,

)
