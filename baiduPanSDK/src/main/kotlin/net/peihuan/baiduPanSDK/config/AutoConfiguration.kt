package net.peihuan.baiduPanSDK.config

import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
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
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
        return okHttpClient
    }

    @Bean
    @ConditionalOnMissingBean
    fun BaiduOAuthRemoteService(properties: BaiduPanProperties): BaiduOAuthRemoteService {
        return BaiduOAuthRemoteService(okHttpClient(), properties)
    }
}