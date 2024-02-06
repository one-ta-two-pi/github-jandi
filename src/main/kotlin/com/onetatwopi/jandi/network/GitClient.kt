package com.onetatwopi.jandi.network
import com.intellij.openapi.application.PathManager
import org.apache.http.HttpHeaders
import org.apache.http.HttpResponse
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import java.io.*
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class GitClient private constructor() {

    companion object {
        var loginId : String? = null
        var userToken : String? = null
        private const val timeout = 3000
        private val apiVersion : String = "2022-11-28"
        private val httpclient : CloseableHttpClient = HttpClientBuilder.create()
            .disableContentCompression()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .build()
            ).build()
        private val userFilePath: Path = Paths.get(PathManager.getPluginsPath() + File.separator + "userInfo.txt")

        init {
            if(Files.exists(userFilePath)) {
                try {
                    val userinfo : UserInfo = readFromUserFile()
                    loginId = userinfo.userId
                    userToken = userinfo.userToken
                } catch (e : Exception) {
                    throw inValidTokenFileException()
                }
            }
        }

        fun login(userToken : String) {
            val request : HttpUriRequest = RequestBuilder.get("https://api.github.com/user")
                .setHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .setHeader(HttpHeaders.AUTHORIZATION, "Bearer $userToken")
                .setHeader("X-GitHub-Api-Version", apiVersion)
                .build()

            val response : HttpResponse = httpclient.execute(request)

            if(response.statusLine.statusCode != 200) {
                throw invalidTokenException()
            }

            loginId = getLoginId(response = response)

            try {
                writeToUserFile(userInfo = UserInfo(userId = loginId!!, userToken = userToken))
            } catch (e : Exception) {
                e.printStackTrace()
                throw cannotWriteFileException()
            }
        }

        private fun writeToUserFile(userInfo: UserInfo) {
            ObjectOutputStream(FileOutputStream(userFilePath.toString())).use { stream ->
                stream.writeObject(userInfo)
            }
        }

        private fun readFromUserFile(): UserInfo {
            ObjectInputStream(FileInputStream(userFilePath.toString())).use { stream ->
                return stream.readObject() as UserInfo
            }
        }

        private fun getLoginId(response: HttpResponse) : String{
            var loginStr : String = response.entity.content.reader(charset = Charset.defaultCharset())
                .readText().split(",")[0].split(":")[1]

            loginStr = loginStr.substring(1, loginStr.length - 1)

            return loginStr
        }

        private fun cannotWriteFileException(): Throwable {
            return RuntimeException("파일에 토큰 정보를 저장하는데에 실패하였습니다.")
        }

        private fun inValidTokenFileException(): Throwable {
            return RuntimeException("파일에서 토큰 정보를 읽어올 수 없습니다.")
        }

        private fun invalidTokenException(): Throwable {
            return RuntimeException("잘못된 토큰을 입력하였습니다.")
        }

    }
}