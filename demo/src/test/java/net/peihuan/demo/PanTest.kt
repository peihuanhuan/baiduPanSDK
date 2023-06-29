package net.peihuan.demo

import net.peihuan.baiduPanSDK.domain.constant.AsyncModel
import net.peihuan.baiduPanSDK.domain.constant.ManageFileOpera
import net.peihuan.baiduPanSDK.domain.dto.CopyOrMoveFileRequest
import net.peihuan.baiduPanSDK.service.BaiduService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class PanTest {

    @Autowired
    lateinit var baiduService: BaiduService

    private val tempAccessToken = "121.251310f03dd077025440930fbfa883dd.YmZ9f6sOjqY10rDAV5bjcTsgX0huzCB_JJEPw1O.hGaXBg"
    private val tempUserAccessToken = "121.d23eaf74ec76578f3bcd17344d1c660d.Y5zsVSaXmULrjarjHYSk4Wttmfh-ICl57CnJPaw.yrR_mg"

    @BeforeEach
    fun init() {
        baiduService.getConfigStorage().updateAccessToken("1", tempAccessToken, 100000)
        baiduService.getConfigStorage().updateAccessToken("real-user", tempUserAccessToken, 100000)
    }




//    @Test
//    fun test_rename() {
//        val x = mapOf("path" to "/apps/阿烫/5.png", "newname" to "6.png")
//        val yy = baiduService.getPanService().managerFile("1", opera = ManageFileOpera.RENAME, fileList = listOf(x), asyncModel = AsyncModel.AUTO)
//        assert(yy)
//    }


    @Test
    fun test_copy_files() {
        val request = CopyOrMoveFileRequest(
                path = "6.png",
                dest = "/",
                newname = "6-copy2.jpg"
        )
        val yy = baiduService.getPanService().copyFile("1", request , asyncModel = AsyncModel.SYNC)
        assert(yy)
    }


    @Test
    fun test_delete_files() {
        val yy = baiduService.getPanService().deleteFile("1",  listOf("6-copy2.jpg"), asyncModel = AsyncModel.SYNC )
        assert(yy)
    }


    @Test
    fun test_list() {
        val list = baiduService.getPanService().listFiles("real-user", "/")
        println()
    }

    @Test
    fun test_filemeta() {
        val xxx = baiduService.getPanService().filemetas("1", listOf(234140919765777))
        println()
    }
    @Test
    fun test_create_dir() {
        val resp = baiduService.getPanService().createDir("1", "/")
        println()
    }

    @Test
    fun test_share() {
        val list = baiduService.getPanService().listFiles("real-user", "/")
        val fids = list.filter { it.category != 6 }.map { it.fs_id }.take(1)
        val shareResp = baiduService.getPanService().shareFiles("real-user", period = 1, fids = fids)
        println("hi，这是我用百度网盘分享的内容~复制这段内容打开「百度网盘」APP即可获取  链接:${shareResp.link} 提取码:${shareResp.pwd}")
        val saveShare = baiduService.getPanService().saveShareLink("1", "/", emptyList(), shareResp)
        assert(saveShare.errno == 0)
    }
}