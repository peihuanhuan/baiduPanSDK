// package net.peihuan.baiduPanSDK.feign.service
//
// import net.peihuan.baiduPanSDK.feign.dto.AuthorizeResponseDTO
// import org.springframework.cloud.openfeign.FeignClient
// import org.springframework.web.bind.annotation.GetMapping
// import org.springframework.web.bind.annotation.RequestParam
//
// @FeignClient(
//     name = "openapi.baidu.oauth",
//     url = "http://openapi.baidu.com",
// )
// interface BaiduOAuthFeignService {
//
//
//
//     @GetMapping("oauth/2.0/authorize")
//     fun authorize(
//         @RequestParam("response_type") response_type: String,
//         @RequestParam("client_id") client_id: String,
//         @RequestParam("redirect_uri") redirect_uri: String,
//         @RequestParam("scope") scope: String = "basic,netdisk",
//         @RequestParam("device_id") device_id: String,
//
//     ): String
//
//
//     @GetMapping("oauth/2.0/token")
//     fun token(
//             @RequestParam("grant_type") grant_type: String,
//             @RequestParam("code") code: String,
//             @RequestParam("redirect_uri") redirect_uri: String,
//             @RequestParam("client_secret") client_secret: String,
//             @RequestParam("client_id") client_id: String,
//
//             ): AuthorizeResponseDTO
//
//
// }