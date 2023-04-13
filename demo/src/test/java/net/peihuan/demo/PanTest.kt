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

    private val tempAccessToken = "121.69a9784492f745ede12c4da824060e5f.YDl8lrWge8L-K4pIIKResbMhmnXPAUTA2YmRyLD.kHXraw"


    @BeforeEach
    fun init() {
        baiduService.getConfigStorage().updateAccessToken("1", tempAccessToken, 100000)
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
}