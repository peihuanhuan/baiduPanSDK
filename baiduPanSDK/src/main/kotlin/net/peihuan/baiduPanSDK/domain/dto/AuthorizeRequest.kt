package net.peihuan.baiduPanSDK.domain.dto

data class AuthorizeRequest(
        val response_type : String,
        val client_id : String,
        val redirect_uri : String,
        val scope : String,
        val device_id : String,

)
