package com.onetatwopi.jandi.data.pullRequest

import com.intellij.openapi.ui.Messages
import com.intellij.util.net.HTTPMethod
import com.onetatwopi.jandi.client.Category
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.client.GitClient.loginId
import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import com.onetatwopi.jandi.layout.dto.PullRequestSubmit
import com.onetatwopi.jandi.layout.panel.TabbedPanel
import kotlinx.serialization.json.*
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

        val headBranch = loginId?.let { head.replace("origin", it) }
        val requestBody = mutableListOf(
                Pair("title", title as Any),
                Pair("body", body as Any),
                Pair("head", headBranch as Any), // "username:develop"
                Pair("base", base as Any), // "master"
        )
        val response = GitClient.repoRequest(
                method = HTTPMethod.POST,
                repo = TabbedPanel.getSelectedRepository(),
                category = Category.PULL,
                body = requestBody
        )

        checkResponseError(response)

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
            url = jsonPullRequest["html_url"]?.jsonPrimitive?.content ?: "",
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

    private fun checkResponseError(response: String) {
        val jsonElement = Json.parseToJsonElement(response)
        val jsonObject = jsonElement.jsonObject

        val message = jsonObject["message"]?.jsonPrimitive?.content
        if (message == "Validation Failed") {
            val errors = jsonObject["errors"]?.jsonArray
            errors?.forEach { error ->
                val errorObj = error.jsonObject
                val errorMessage = errorObj["message"]?.jsonPrimitive?.content

                if (errorMessage != null) {
                    if (errorMessage.indexOf("already exists") > -1) {
                        Messages.showMessageDialog("이미 존재하는 pull request 입니다.", "Error", Messages.getErrorIcon())
                        throw RuntimeException("이미 존재하는 pull request 입니다.")
                    } else if (errorMessage.indexOf("No commits between") > -1) {
                        Messages.showMessageDialog("요청하신 head > base 브랜치 사이에 커밋이 없습니다.", "Error", Messages.getErrorIcon())
                        throw RuntimeException("요청하신 head > base 브랜치 사이에 커밋이 없습니다.")
                    }
                }
            }
        }
    }
}