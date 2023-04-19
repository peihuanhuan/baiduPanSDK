package net.peihuan.baiduPanSDK.service.remote

import com.google.gson.Gson
import mu.KotlinLogging
import net.peihuan.baiduPanSDK.config.BaiduPanProperties
import net.peihuan.baiduPanSDK.domain.constant.AsyncModel
import net.peihuan.baiduPanSDK.domain.constant.ManageFileOpera
import net.peihuan.baiduPanSDK.domain.dto.*
import net.peihuan.baiduPanSDK.exception.BaiduPanException
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URLDecoder


class BaiduPanRemoteService(
    private val okHttpClient: OkHttpClient,
    private val baiduPanProperties: BaiduPanProperties

) {

    private val gson = Gson()

    private val BASE_URL = "http://pan.baidu.com"

    private val log = KotlinLogging.logger {}


    fun precreate(
        accessToken: String,
        path: String,
        size: Long,
        isdir: Boolean,
        block_list: List<String>,
        rtype: RtypeEnum = RtypeEnum.RENAME
    ): PrecreateResponseDTO {

        val url = "${BASE_URL}/rest/2.0/xpan/file".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "precreate")
            .addQueryParameter("access_token", accessToken)
            .build()

        val requestBody: RequestBody = FormBody.Builder()
            .add("path", path)
            .add("size", size.toString())
            .add("isdir", if (isdir) "1" else "0")
            .add("block_list", gson.toJson(block_list))
            .add("autoinit", "1")
            .add("rtype", rtype.code)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, PrecreateResponseDTO::class.java)

    }

    fun superfile2(
        accessToken: String,
        path: String,
        uploadid: String,
        partseq: Int,
        file: ByteArray
    ): UploadResponseDTO {

        val url = "https://d.pcs.baidu.com/rest/2.0/pcs/superfile2".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "upload")
            .addQueryParameter("access_token", accessToken)
            .addQueryParameter("path", path)
            .addQueryParameter("type", "tmpfile")
            .addQueryParameter("uploadid", uploadid)
            .addQueryParameter("partseq", partseq.toString())
            .build()


        val requestBody: RequestBody = RequestBody.create("text/plain;charset=utf-8".toMediaType(), file)
        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        log.info("baidupan superfile2  resp:{} req: {}", json, url.toString())
        return gson.fromJson(json, UploadResponseDTO::class.java)

    }


    fun create(
        accessToken: String,
        path: String,
        size: Long?,
        uploadid: String?,
        isdir: Boolean,
        block_list: List<String> ? ,
        rtype: RtypeEnum = RtypeEnum.RENAME
    ): CreateResponseDTO {

        val url = "${BASE_URL}/rest/2.0/xpan/file".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "create")
            .addQueryParameter("access_token", accessToken)
            .build()

        val requestBody: RequestBody = FormBody.Builder()
            .add("path", path)
            .apply { size?.let { this.add("size", size.toString()) } }
            .add("isdir", if (isdir) "1" else "0")
            .apply { block_list?.let { this.add("block_list", gson.toJson(block_list)) } }
            .apply { uploadid?.let { this.add("uploadid", uploadid) } }
            .add("autoinit", "1")
            .add("rtype", rtype.code)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, CreateResponseDTO::class.java)

    }

    fun share(
        accessToken: String,
        schannel: Int,
        third_type: Int,
        csign: String,
        period: Int,
        fid_list: List<Long>,
        description: String = "",
    ): ShareResponseDTO {
        val url = "${BASE_URL}/rest/2.0/xpan/share/set".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "create")
            .addQueryParameter("access_token", accessToken)
            .build()

        val requestBody: RequestBody = FormBody.Builder()
            .add("fid_list", gson.toJson(fid_list))
            .add("schannel", schannel.toString())
            .add("period", period.toString())
            .add("third_type", third_type.toString())
            .add("description", description)
            .add("csign", csign)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, ShareResponseDTO::class.java)
    }

    fun verifyShareLink(
            accessToken: String,
            shareid: Long,
            uk: Long,
            pwd: String,
            third_type: Int,
            redirect: Int,
    ): VerifyShareLinkResponseDTO {
        val url = "${BASE_URL}/rest/2.0/xpan/share".toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("method", "verify")
                .addQueryParameter("shareid", shareid.toString())
                .addQueryParameter("uk", uk.toString())
                .addQueryParameter("access_token", accessToken)
                .build()

        val requestBody: RequestBody = FormBody.Builder()
                .add("pwd", pwd)
                .add("third_type", third_type.toString())
                .add("redirect", redirect.toString())
                .build()
        val request = Request.Builder()
                .url(url)
                .header("Referer", "pan.baidu.com")
                .post(requestBody)
                .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        val resp = gson.fromJson(json, VerifyShareLinkResponseDTO::class.java)
        if (resp.errno !=  0) {
            log.error("verify share link error: response: {}", json)
        }
        return resp
    }

    fun getShareFiles(
            accessToken: String,
            shareid: Long,
            uk: Long,
            sekey: String,
            page: Int,
            num: Int,
    ): GetShareLinkFilesResponseDTO {
        val url = "${BASE_URL}/rest/2.0/xpan/share".toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("method", "list")
                .addQueryParameter("shareid", shareid.toString())
                .addQueryParameter("uk", uk.toString())
                .addQueryParameter("access_token", accessToken)
                .addQueryParameter("page", page.toString())
                .addQueryParameter("num", num.toString())
                .addQueryParameter("root", "1")
                // 接口返回的已经是 encode 之后的了，要decode，防止再次被 encode
                .addQueryParameter("sekey", URLDecoder.decode(sekey))
                .build()

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        val resp = gson.fromJson(json, GetShareLinkFilesResponseDTO::class.java)
        if (resp.errno !=  0) {
            log.error("get share link files error: response: {}", json)
        }
        return resp
    }

    fun saveShareLink(
            accessToken: String,
            shareid: Long,
            from: Long,
            sekey: String,
            path: String,
            fsidlist: List<Long>,
            async: Int,
            ondup:String = "newcopy",
    ): SaveShareLinkResponseDTO {
        val url = "${BASE_URL}/rest/2.0/xpan/share".toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("method", "transfer")
                .addQueryParameter("shareid", shareid.toString())
                .addQueryParameter("from", from.toString())
                .addQueryParameter("access_token", accessToken)
                .addQueryParameter("sekey", sekey)
                .build()

        val requestBody: RequestBody = FormBody.Builder()
                .add("fsidlist", gson.toJson(fsidlist))
                .add("path", path)
                .add("async", async.toString())
                .add("ondup", ondup)
                .add("sekey", URLDecoder.decode(sekey))
                .build()
        val request = Request.Builder()
                .url(url)
                .header("Referer", "pan.baidu.com")
                .post(requestBody)
                .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        val resp = gson.fromJson(json, SaveShareLinkResponseDTO::class.java)
        if (resp.errno !=  0) {
            log.error("save share link error: response: {}", json)
        }
        return resp
    }


    fun filemetas(
        accessToken: String,
        dlink: Int,
        path: String?,
        fid_list: List<Long>,
    ): FilemetasResp {
        val urlBuilder = "${BASE_URL}/rest/2.0/xpan/multimedia".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "filemetas")
            .addQueryParameter("access_token", accessToken)
            .addQueryParameter("fsids", gson.toJson(fid_list))
            .addQueryParameter("dlink", dlink.toString())
        if (path == null) {
            urlBuilder.addQueryParameter("path", path)
        }

        val request = Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        return gson.fromJson(json, FilemetasResp::class.java)
    }

    fun manageFile(
            accessToken: String,
            opera: ManageFileOpera,
            async: AsyncModel,
            filelist:List<Any>,
            ondup: String = "override",
    ): ManageFileResp {
        val urlBuilder = "${BASE_URL}/rest/2.0/xpan/file".toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("method", "filemanager")
                .addQueryParameter("access_token", accessToken)
                .addQueryParameter("opera", opera.code)
                .addQueryParameter("device_id", baiduPanProperties.deviceId)


        val requestBody: RequestBody = FormBody.Builder()
                .add("async", async.code)
                .add("filelist", gson.toJson(filelist))
                .add("ondup", ondup)
                .build()

        val request = Request.Builder()
                .url(urlBuilder.build())
                .post(requestBody)
                .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()

        val resp = gson.fromJson(json, ManageFileResp::class.java)
        if (resp.errno !=  0) {
            log.error("manager file error: response: {}", json)
        }
        return resp
    }

    fun listFiles(
        accessToken: String,
        dir	: String,
        order: String = "name",
        desc: Int = 1,
        start: Int = 0,
        limit: Int = 1000,
        folder: Int = 0,
    ): List<Filemeta> {
        val urlBuilder = "${BASE_URL}/rest/2.0/xpan/file".toHttpUrlOrNull()!!.newBuilder()
            .addQueryParameter("method", "list")
            .addQueryParameter("access_token", accessToken)
            .addQueryParameter("dir", dir)
            .addQueryParameter("order", order)
            .addQueryParameter("desc", desc.toString())
            .addQueryParameter("start", start.toString())
            .addQueryParameter("limit", limit.toString())
            .addQueryParameter("folder", folder.toString())

        val request = Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val json = response.body?.string()
        val resp = gson.fromJson(json, ListFilesResp::class.java)
        if (resp.errno != 0) {
            throw BaiduPanException("查询列表失败 resp: $json")
        }
        return resp.list ?: emptyList()
    }

}