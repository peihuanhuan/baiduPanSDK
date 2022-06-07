package net.peihuan.demo

import net.peihuan.baiduPanSDK.service.BaiduService
import net.peihuan.baiduPanSDK.service.impl.PanServiceImpl
import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
import net.peihuan.baiduPanSDK.service.remote.BaiduPanRemoteService
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
        val x  = baiduService.getTokenByCode("1","6eedb80e7befd6547de37fc091f917fa", "http://baidupan.dejavuu.cn")
        println(x)
    }


    @Test
    fun test_upload() {
//        panService.uploadFile("filename.docx", File("C:\\Users\\Administrator\\Desktop\\裴欢_Java后端_简历.docx"))
//        baiduService.getConfigStorage().setRefreshToken("1", "122.276dae353bcddf320eabd84a31cd1d74.Ym81okSHrBgyxHPssG02IthTRVLgDEy4gzWgYMp.mU6tlg",1000)
        baiduService.getConfigStorage().updateAccessToken("1", "121.f8e08d627710f651801a01ae45da0389.YgWnPC6vCkFNVNuba1KtJCKlsGiwZQ4rKhGhsyx.1R9Ynw", 100000)
        println(baiduService.getAccessToken("1"))
//        val x = baiduService.getPanService().uploadFile("1","反反复复/牛啊.mp3", File("C:\\Users\\Administrator\\Desktop\\【双语熟Vox Akuma】daddy读...1138-20211228-012650.mp3"))
        val yy = baiduService.getPanService().shareFiles("1", listOf(606478106687463), 1)
        println(yy)
    }
}