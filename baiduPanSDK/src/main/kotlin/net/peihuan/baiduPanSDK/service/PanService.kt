package net.peihuan.baiduPanSDK.service

import net.peihuan.baiduPanSDK.domain.dto.CreateResponseDTO
import java.io.File

interface PanService {
    fun uploadFile(userId: String, path: String, file: File): CreateResponseDTO
}