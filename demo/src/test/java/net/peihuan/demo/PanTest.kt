package net.peihuan.demo

import net.peihuan.baiduPanSDK.service.BaiduService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
class PanTest {

    @Autowired
    lateinit var baiduService: BaiduService

    // 121.a20fc8ef73aa0d1868de48da7938f738.YQZNsbc_cxHpUG54kGQj-s3wruO_XCzNn-IlbY-.n_QArQ
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
        val yy = baiduService.getPanService().uploadFile("1","【白噪音|环境音】苏菲的帽子店 布料剪裁 羊皮卷尺 缝纫声 哈尔的移动城堡 吉卜力动画氛围音 背景音.mp3", File("C:\\Users\\Administrator\\Desktop\\裴欢_Java后端_简历.docx"))
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