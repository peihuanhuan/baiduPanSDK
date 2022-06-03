package net.peihuan.demo

import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication


@ConfigurationPropertiesScan
@SpringBootApplication
class BaiduPanSdkApplication

fun main(args: Array<String>) {

	val context = SpringApplication.run(BaiduPanSdkApplication::class.java, *args)
	System.out.println(context.getBean(BaiduOAuthRemoteService::class.java))
}
