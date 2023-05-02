package net.peihuan.demo

import net.peihuan.baiduPanSDK.domain.constant.AsyncModel
import net.peihuan.baiduPanSDK.domain.constant.ManageFileOpera
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

    private val tempAccessToken = "121.9d64b72ef703f27ba1b5944c53fb6198.YHHndLUfJbTJF_0dHukkjk2Y7ecAi-8KFsP8EKp.06lLZg"
    private val tempUserAccessToken = "121.7c7331eb8f8d237bbcf68b6cd4183755.YBEUaPLxO-8bg42qdlrP6dazfFEBkgxlvuUEFln.Zyq1Tw"


    @BeforeEach
    fun init() {
        baiduService.getConfigStorage().updateAccessToken("1", tempAccessToken, 100000)
        baiduService.getConfigStorage().updateAccessToken("real-user", tempUserAccessToken, 100000)
    }


    @Test
    fun test_delete_files() {
        val deleteResult = baiduService.getPanService().managerFile("1", opera = ManageFileOpera.DELETE, fileList = listOf("回梦游仙.png"), asyncModel = AsyncModel.AUTO)
        assert(deleteResult)
    }


    @Test
    fun test_copy_files() {
        val x = mapOf("path" to "/apps/阿烫/回梦游仙.png", "newname" to "2.png")
        val yy = baiduService.getPanService().managerFile("1", opera = ManageFileOpera.RENAME, fileList = listOf(x), asyncModel = AsyncModel.AUTO)
        assert(yy)
    }

    @Test
    fun test_list() {
        val list = baiduService.getPanService().listFiles("1", "/保存share的文件/2023奥斯卡最佳动画：男孩、鼹鼠、狐狸和马.mp4")
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