package net.peihuan.baiduPanSDK.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "baidu-pan")
class BaiduPanProperties(){
        lateinit var clientId: String
        lateinit var deviceId: String
        lateinit var clientSecret: String
}

