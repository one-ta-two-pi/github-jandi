package com.onetatwopi.jandi.data.pullRequest

import com.intellij.util.net.HTTPMethod
import com.onetatwopi.jandi.client.Category
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import com.onetatwopi.jandi.layout.dto.PullRequestSubmit
import com.onetatwopi.jandi.layout.panel.TabbedPanel
import kotlinx.serialization.json.*
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PullRequestService private constructor() {

    companion object {
        private const val MAX_PULL_REQUEST = 10

        val instance: PullRequestService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PullRequestService()
        }
    }

    init {
        parsePullRequestList()
    }

    private fun getRepositoryPullRequests(): String {

        return GitClient.repoRequest(
                method = HTTPMethod.GET,
                repo = TabbedPanel.getSelectedRepository(),
                category = Category.PULL,
        )
    }

    private fun getRepositoryPullRequestDetail(number: Int): String = GitClient.repoRequest(
            method = HTTPMethod.GET,
            repo = TabbedPanel.getSelectedRepository(),
            category = Category.PULL,
            number = number
    )

    private fun createRepositoryPullRequest(pullRequestSubmit: PullRequestSubmit): String {
        val (title, body, head, base) = pullRequestSubmit

        val requestBody = mutableMapOf(
                "title" to title,
                "body" to body,
                "head" to head,
                "base" to base
        )

        val requestBodyList: List<NameValuePair> = requestBody.map { entry ->
            BasicNameValuePair(entry.key, entry.value)
        }

        println(TabbedPanel.getSelectedRepository())
        val response = GitClient.repoRequest(
                method = HTTPMethod.POST,
                repo = TabbedPanel.getSelectedRepository(),
                category = Category.PULL,
                body = requestBodyList
        )

//        checkResponseError(response)

        return response
    }

    fun parsePullRequestList(): MutableList<PullRequestInfo> {
        val response = getRepositoryPullRequests()

        val jsonPullRequestRes = Json.parseToJsonElement(response).jsonArray
        val pullRequests = mutableListOf<PullRequestInfo>()
        for (i in 0 until jsonPullRequestRes.size) {
            val jsonPullRequest = jsonPullRequestRes[i].jsonObject
            pullRequests.add(generatePullRequest(jsonPullRequest))
        }

        val sortedPullRequest = pullRequests.sortedByDescending { it.createdAt }
        val limitedPullRequests = sortedPullRequest.take(MAX_PULL_REQUEST)

        return limitedPullRequests.toMutableList()
    }

    fun createPullRequest(PullRequestSubmit: PullRequestSubmit) {
        createRepositoryPullRequest(PullRequestSubmit)
    }

    private fun generatePullRequest(jsonPullRequest: JsonObject): PullRequestInfo {
        val isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val customDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val createdAt =
            convertDateString(jsonPullRequest["created_at"]?.toString(), isoDateTimeFormatter, customDateTimeFormatter)
        val updatedAt =
            convertDateString(jsonPullRequest["updated_at"]?.toString(), isoDateTimeFormatter, customDateTimeFormatter)

        return PullRequestInfo(
            title = jsonPullRequest["title"]?.jsonPrimitive?.content ?: "",
            requestUserId = jsonPullRequest["user"]?.jsonObject?.get("login")?.jsonPrimitive?.content ?: "",
            status = jsonPullRequest["state"]?.jsonPrimitive?.content ?: "",
            url = jsonPullRequest["url"]?.jsonPrimitive?.content ?: "",
            createdAt = createdAt ?: "yyyy-MM-dd HH:mm:ss",
            updatedAt = updatedAt ?: "yyyy-MM-dd HH:mm:ss"
        )
    }

    private fun convertDateString(
        input: String?,
        fromFormatter: DateTimeFormatter,
        toFormatter: DateTimeFormatter,
    ): String? {
        return try {
            input?.trim('"')?.let {
                LocalDateTime.parse(it, fromFormatter).format(toFormatter)
            }
        } catch (e: Exception) {
//            throw RuntimeException("Fail to convert local time.")
            return null
        }
    }
}