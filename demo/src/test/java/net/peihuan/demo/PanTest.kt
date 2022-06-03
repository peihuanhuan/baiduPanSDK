package net.peihuan.demo

import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PanTest {

    @Autowired
    lateinit var baiduOAuthRemoteService: BaiduOAuthRemoteService


    @Test
    fun test() {
        baiduOAuthRemoteService.authorize("xxx")

    }
}