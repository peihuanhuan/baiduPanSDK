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
class SimpleTest {


    @Autowired
    lateinit var baiduService: BaiduService

    private val tempAccessToken = "121.69a9784492f745ede12c4da824060e5f.YDl8lrWge8L-K4pIIKResbMhmnXPAUTA2YmRyLD.kHXraw"


    @BeforeEach
    fun init() {
        baiduService.getConfigStorage().updateAccessToken("1", tempAccessToken, 100000)
    }


    @Test
    fun test_authorize() {
        val x = baiduService.getAuthorizeUrl("http://baidupan.dejavuu.cn")
        println(x)
    }

    @Test
    fun test_getToken(){
        val x  = baiduService.getAccessToken("1","b7c366a9d73121a76b9a32ee313ec460", "http://baidupan.dejavuu.cn")
        println(x)
    }

    @Test
    fun test_upload() {
//        panService.uploadFile
//        baiduService.getConfigStorage().setRefreshToken("1", "122.276dae353bcddf320eabd84a31cd1d74.Ym81okSHrBgyxHPssG02IthTRVLgDEy4gzWgYMp.mU6tlg",1000)
        baiduService.getConfigStorage().updateAccessToken("1", "121.f8e08d627710f651801a01ae45da0389.YgWnPC6vCkFNVNuba1KtJCKlsGiwZQ4rKhGhsyx.1R9Ynw", 100000)
        println(baiduService.getAccessToken("1"))
//        val x = baiduService.getPanService().uploadFile("1","反反复复/牛啊.mp3", File("C:\\Users\\Administrator\\Desktop\\【双语熟Vox Akuma】daddy读...1138-20211228-012650.mp3"))
        val yy = baiduService.getPanService().uploadFile("1"," /【中抓】水千丞原著《你却爱着一个》聚会番外.mp3", File("C:\\Users\\Administrator\\Desktop\\裴欢_Java后端_简历.docx"))
        println(yy)
    }


    @Test
    fun test_share() {
//        panService.uploadFile("filename.docx", File("C:\\Users\\Administrator\\Desktop\\裴欢_Java后端_简历.docx"))
//        baiduService.getConfigStorage().setRefreshToken("1", "122.276dae353bcddf320eabd84a31cd1d74.Ym81okSHrBgyxHPssG02IthTRVLgDEy4gzWgYMp.mU6tlg",1000)
        baiduService.getConfigStorage().updateAccessToken("1", "121.f8e08d627710f651801a01ae45da0389.YgWnPC6vCkFNVNuba1KtJCKlsGiwZQ4rKhGhsyx.1R9Ynw", 100000)
        println(baiduService.getAccessToken("1"))
//        val x = baiduService.getPanService().uploadFile("1","反反复复/牛啊.mp3", File("C:\\Users\\Administrator\\Desktop\\【双语熟Vox Akuma】daddy读...1138-20211228-012650.mp3"))
        val yy = baiduService.getPanService().shareFiles("1", listOf(1053773202214064 ), 1)
        println(yy)
    }

    @Test
    fun test_list_files() {

        baiduService.getConfigStorage().updateAccessToken("1", "121.f8e08d627710f651801a01ae45da0389.YgWnPC6vCkFNVNuba1KtJCKlsGiwZQ4rKhGhsyx.1R9Ynw", 100000)
        println(baiduService.getAccessToken("1"))
        val yy = baiduService.getPanService().listFiles("1", dir = "oIWc_51xYURq_7jNCfrr40dc0q3Q")
        println(yy)
    }



}