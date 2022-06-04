package net.peihuan.demo

import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
import net.peihuan.baiduPanSDK.service.remote.BaiduPanRemoteService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PanTest {

    @Autowired
    lateinit var baiduOAuthRemoteService: BaiduOAuthRemoteService
    @Autowired
    lateinit var baiduPanRemoteService: BaiduPanRemoteService


    @Test
    fun test_authorize() {
        val x = baiduOAuthRemoteService.authorize("http://baidupan.dejavuu.cn")
        println(x)
    }

    @Test
    fun test_getToken(){
        val x  = baiduOAuthRemoteService.getToken("0fb8ab7d6decbdb52b628ea0a39f8165", "http://baidupan.dejavuu.cn")
        println(x)
    }

    @Test
    fun test_precreate() {
        val x = baiduPanRemoteService.precreate("121.a20fc8ef73aa0d1868de48da7938f738.YQZNsbc_cxHpUG54kGQj-s3wruO_XCzNn-IlbY-.n_QArQ",
        "xxx",
            100,
            false,
            listOf("xxx", "xxx")
        )

        println(x)
    }
}