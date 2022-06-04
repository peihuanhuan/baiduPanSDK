package net.peihuan.baiduPanSDK.service

import net.peihuan.baiduPanSDK.service.remote.BaiduOAuthRemoteService
import net.peihuan.baiduPanSDK.service.remote.BaiduPanRemoteService
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream

@Service
class BaiduOauthService(private val baiduOauthService: BaiduOAuthRemoteService) {

    fun getAccessToken(): String {
        return "121.a20fc8ef73aa0d1868de48da7938f738.YQZNsbc_cxHpUG54kGQj-s3wruO_XCzNn-IlbY-.n_QArQ"
    }


}