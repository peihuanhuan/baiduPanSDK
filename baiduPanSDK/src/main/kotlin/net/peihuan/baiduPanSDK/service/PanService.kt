package net.peihuan.baiduPanSDK.service

import net.peihuan.baiduPanSDK.domain.constant.AsyncModel
import net.peihuan.baiduPanSDK.domain.constant.ManageFileOpera
import net.peihuan.baiduPanSDK.domain.constant.SaveShareAsyncModel
import net.peihuan.baiduPanSDK.domain.dto.*
import java.io.File

interface PanService {

    fun filemetas(userId: String, fsids: List<Long>, path: String? = null, dlink: Int = 0): List<Filemeta>

    fun listFiles(userId: String, dir: String, order: String = "name", desc: Int = 1, start: Int = 0, limit: Int = 1000, folder: Int = 0): List<Filemeta>

    fun uploadFile(userId: String, path: String, file: File, rtype: RtypeEnum = RtypeEnum.RENAME): CreateResponseDTO
    fun createDir(userId: String, path: String, rtype: RtypeEnum = RtypeEnum.RETURN_ERROR)

    fun shareFiles(userId: String, fids: List<Long>, period: Int, desc: String = ""): ShareResponseDTO

    // savePath 必须要真实存在已创建!
    fun saveShareLink(userId: String, savePath:String, fs_ids: List<Long> = emptyList(), share: ShareResponseDTO, async: SaveShareAsyncModel = SaveShareAsyncModel.SYNC) : SaveShareLinkResponseDTO

//    fun managerFile(userId: String, opera: ManageFileOpera, asyncModel: AsyncModel, fileList: List<Any>, ondup :String = "newcopy"): Boolean

    fun deleteFile(userId: String, filePath: List<String> , asyncModel: AsyncModel): Boolean

    /**
     * ondup:  全局ondup,遇到重复文件的处理策略, fail(直接返回失败)、newcopy(重命名文件)、overwrite、skip   (测试overwrite没效果)
     */
    fun copyFile(userId: String, request: CopyOrMoveFileRequest, asyncModel: AsyncModel, ondup :String = "newcopy"): Boolean


}