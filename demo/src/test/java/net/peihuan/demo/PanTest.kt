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
        val x  = baiduService.getTokenByCode("1","0fb8ab7d6decbdb52b628ea0a39f8165", "http://baidupan.dejavuu.cn")
        println(x)
    }


    @Test
    fun test_upload() {
//        panService.uploadFile("filename.docx", File("C:\\Users\\Administrator\\Desktop\\裴欢_Java后端_简历.docx"))
        val x = baiduService.getPanService().uploadFile("1","xxx.exe", File("C:\\Users\\Administrator\\Desktop\\tieba_tool\\chromedriver.exe"))

    }
}