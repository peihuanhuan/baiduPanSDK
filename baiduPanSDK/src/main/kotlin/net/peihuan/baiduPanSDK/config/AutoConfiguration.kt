package net.peihuan.baiduPanSDK.config

import net.peihuan.baiduPanSDK.service.BaiduService
import net.peihuan.baiduPanSDK.service.impl.BaiduServiceImpl
import okhttp3.OkHttpClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit


@Configuration
@EnableConfigurationProperties(BaiduPanProperties::class)
class AutoConfiguration() {

    @Bean
    @ConditionalOnMissingBean
    fun okHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        return okHttpClient
    }

    @Bean
    @ConditionalOnMissingBean
    fun baiduService(okHttpClient: OkHttpClient, properties: BaiduPanProperties): BaiduService {
        return BaiduServiceImpl(properties, okHttpClient)
    }
}