package net.peihuan.baiduPanSDK.service

import net.peihuan.baiduPanSDK.domain.constant.AsyncModel
import net.peihuan.baiduPanSDK.domain.constant.ManageFileOpera
import net.peihuan.baiduPanSDK.domain.dto.CreateResponseDTO
import net.peihuan.baiduPanSDK.domain.dto.Filemeta
import net.peihuan.baiduPanSDK.domain.dto.RtypeEnum
import net.peihuan.baiduPanSDK.domain.dto.ShareResponseDTO
import java.io.File

interface PanService {

    fun filemetas(userId: String, fsids: List<Long>, path: String? = null, dlink: Int = 0): List<Filemeta>

    fun listFiles(userId: String, dir: String, order: String = "name", desc: Int = 1, start: Int = 0, limit: Int = 1000, folder: Int = 0): List<Filemeta>

    fun uploadFile(userId: String, path: String, file: File, rtype: RtypeEnum = RtypeEnum.RENAME): CreateResponseDTO

    fun shareFiles(userId: String, fids: List<Long>, period: Int, desc: String = ""): ShareResponseDTO

    fun managerFile(userId: String, opera: ManageFileOpera, asyncModel: AsyncModel, fileList: List<Any>, ondup :String = "override"): Boolean
}