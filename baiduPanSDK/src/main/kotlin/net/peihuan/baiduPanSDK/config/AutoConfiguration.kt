package net.peihuan.baiduPanSDK.config

import net.peihuan.baiduPanSDK.service.BaiduOauthService
import net.peihuan.baiduPanSDK.service.PanService
import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
import net.peihuan.baiduPanSDK.service.remote.BaiduPanRemoteService
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
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
    fun baiduOAuthRemoteService(properties: BaiduPanProperties): BaiduOAuthRemoteService {
        return BaiduOAuthRemoteService(okHttpClient(), properties)
    }

    @Bean
    @ConditionalOnMissingBean
    fun baiduPanRemoteService(properties: BaiduPanProperties): BaiduPanRemoteService {
        return BaiduPanRemoteService(okHttpClient(), properties)
    }

    @Bean
    @ConditionalOnMissingBean
    fun panService(properties: BaiduPanProperties): PanService {
        return PanService(baiduPanRemoteService(properties), BaiduOauthService(baiduOAuthRemoteService(properties)), properties)
    }
}