package net.peihuan.baiduPanSDK.domain.dto

enum class RtypeEnum(val code:String, msg: String) {
    RETURN_ERROR("0", "不进行重命名，若云端存在同名文件返回错误"),
    RENAME("1", "当path冲突时，进行重命名"),
    SMART_RENAME("2", "当path冲突且block_list不同时，进行重命名"),
    OVERRIDE("3", "当云端存在同名文件时，对该文件进行覆盖"),
}