package net.peihuan.baiduPanSDK.domain.dto

data class CopyOrMoveFileRequest(
        val newname : String,
        var dest : String,
        var path : String,
)
