package net.peihuan.baiduPanSDK.service

import net.peihuan.baiduPanSDK.domain.dto.CreateResponseDTO
import net.peihuan.baiduPanSDK.domain.dto.ShareResponseDTO
import java.io.File

interface PanService {
    fun uploadFile(userId: String, path: String, file: File): CreateResponseDTO

    fun shareFiles(userId: String, fids: List<Long>, period: Int, desc: String = ""): ShareResponseDTO
}