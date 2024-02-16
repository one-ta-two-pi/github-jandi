package com.onetatwopi.jandi.client

import com.google.gson.Gson
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.notificationGroup
import com.intellij.util.net.HTTPMethod
import com.onetatwopi.jandi.layout.notification.NotificationFactory
import com.onetatwopi.jandi.listener.LoginIdChangeNotifier
import com.onetatwopi.jandi.login.LoginActivity
import com.onetatwopi.jandi.project.ProjectRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.apache.http.HttpHeaders
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import java.io.*
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object GitClient {
    val loginId: String? get() = inputId
    private val userToken: String? get() = inputToken
    private var inputId: String? = null
    private var inputToken: String? = null
    val repos: ImmutableList<String> get() = inputRepos
    private lateinit var inputRepos: ImmutableList<String>
    private val gson = Gson()

    private const val timeout = 2000
    private const val apiVersion: String = "2022-11-28"
    private val httpclient: CloseableHttpClient = HttpClientBuilder.create()
        .disableContentCompression()
        .setDefaultRequestConfig(
            RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build()
        ).build()
    private val userFilePath: Path = Paths.get(PathManager.getPluginsPath() + File.separator + "userInfo.txt")
    private const val repoUrl : String = "https://api.github.com/repos/"

    init {
        if (Files.exists(userFilePath)) {
            val userToken: String = try {
                readFromUserFile()
            } catch (e: Exception) {
                throw inValidTokenFileException()
            }

            try {
                val userId: String = requestGitLogin(userToken = userToken)
                inputId = userId
                inputToken = userToken
                setRepos()
            } catch (e: Exception) {
                LoginIdChangeNotifier.notifyLoginIdChanged(newLoginId = null, isUpdate = false)
            }
        }
    }

    private fun setRepos() {
        val request: HttpUriRequest = RequestBuilder.get("https://api.github.com/users/$loginId/repos")
            .setHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .setHeader(HttpHeaders.AUTHORIZATION, "Bearer $userToken")
            .setHeader("X-GitHub-Api-Version", apiVersion)
            .build()

        val response: HttpResponse = httpclient.execute(request)

        if (response.statusLine.statusCode != 200) {
            throw RuntimeException("권한이 없습니다.")
        }

        try {
            val jsonArray = gson.fromJson<List<Map<String, Any>>>(
                response.entity.content.reader(charset = Charset.defaultCharset())
                    .readText(), List::class.java
            )
            inputRepos = jsonArray.map { it["name"] as String }.toImmutableList()
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("repo 가져오는 중 파싱 오류 발생")
        }
    }

    fun repoRequest(method : HTTPMethod, repo : String, category: Category, body : List<Pair<String, Any>>, number : Int) : String {
        val reqUrl = "$repoUrl$loginId/$repo/${category.value}/$number"
        val request: HttpUriRequest = makeRepoRequest(url = reqUrl, method = method, body = body)

        return getRepoResponse(request = request)
    }

    fun repoRequest(method : HTTPMethod, repo : String, category: Category, number : Int) : String {
        val reqUrl = "$repoUrl$loginId/$repo/${category.value}/$number"
        val request: HttpUriRequest = makeRepoRequest(url = reqUrl, method = method)

        return getRepoResponse(request = request)
    }

    fun repoRequest(method : HTTPMethod, repo : String, category: Category, body : List<Pair<String, Any>>) : String {
        val reqUrl = "$repoUrl$loginId/$repo/${category.value}"
        val request: HttpUriRequest = makeRepoRequest(url = reqUrl, method = method, body = body)
        return getRepoResponse(request = request)
    }

    fun repoRequest(method : HTTPMethod, repo : String, category: Category) : String {
        val reqUrl = "$repoUrl$loginId/$repo/${category.value}"
        val request: HttpUriRequest = makeRepoRequest(url = reqUrl, method = method)

        return getRepoResponse(request = request)
    }

    private fun getRepoResponse(request: HttpUriRequest): String {
        val response = httpclient.execute(request)

        /* TODO : 권한 부족할 때 토큰 처리 */
        if(response.statusLine.statusCode == 401 || response.statusLine.statusCode == 403) {
           NotificationFactory.notify("Not authorized", NotificationType.WARNING)
        }

        return response.entity.content.reader(charset = Charset.defaultCharset())
            .readText()
    }

    private fun makeRepoRequest(method: HTTPMethod, url: String, body: List<Pair<String, Any>>): HttpUriRequest {
        return when(method) {
            HTTPMethod.GET -> RequestBuilder.get(url)
            HTTPMethod.PUT -> RequestBuilder.put(url)
            HTTPMethod.PATCH -> RequestBuilder.patch(url)
            HTTPMethod.POST -> RequestBuilder.post(url)
            else -> { throw RuntimeException("정의 되지 않은 http 메소드 요청") }
        }.apply {
            addHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            addHeader(HttpHeaders.AUTHORIZATION, "Bearer $userToken")
            addHeader("X-GitHub-Api-Version", apiVersion)
            entity = StringEntity(gson.toJson(body.toMap()), ContentType.APPLICATION_JSON)
        }.build()
    }

    private fun makeRepoRequest(method: HTTPMethod, url: String): HttpUriRequest {
        return when(method) {
            HTTPMethod.GET -> RequestBuilder.get(url)
            HTTPMethod.PUT -> RequestBuilder.put(url)
            HTTPMethod.PATCH -> RequestBuilder.patch(url)
            HTTPMethod.POST -> RequestBuilder.post(url)
            else -> { throw RuntimeException("정의 되지 않은 http 메소드 요청") }
        }.apply {
            addHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            addHeader(HttpHeaders.AUTHORIZATION, "Bearer $userToken")
            addHeader("X-GitHub-Api-Version", apiVersion)
        }.build()
    }

    private fun requestGitLogin(userToken : String) : String {
        val request: HttpUriRequest = RequestBuilder.get("https://api.github.com/user")
            .setHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
            .setHeader(HttpHeaders.AUTHORIZATION, "Bearer $userToken")
            .setHeader("X-GitHub-Api-Version", apiVersion)
            .build()

        val response: HttpResponse =  httpclient.execute(request)
        if (response.statusLine.statusCode != 200) {
            throw invalidTokenException()
        }

        return getLoginIdByResponse(response = response)
    }

    fun login(userToken: String) {
        inputId = requestGitLogin(userToken = userToken)
        val wasTokenExist = inputToken != null
        inputToken = userToken
        setRepos()

        try {
            LoginIdChangeNotifier.notifyLoginIdChanged(newLoginId = loginId, isUpdate = wasTokenExist)
        } catch (e : Exception) {
            e.printStackTrace()
        }
        try {
            writeToUserFile(userToken = userToken)
        } catch (e: Exception) {
            e.printStackTrace()
            throw cannotWriteFileException()
        }
    }

    private fun writeToUserFile(userToken: String) {
        val file = File(userFilePath.toString())
        file.writeText(userToken)
    }

    private fun readFromUserFile(): String {
        val file = File(userFilePath.toString())
        return file.readText()
    }

    private fun getLoginIdByResponse(response: HttpResponse): String {
        var loginStr: String = response.entity.content.reader(charset = Charset.defaultCharset())
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