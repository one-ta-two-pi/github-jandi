package com.onetatwopi.jandi.data.pullRequest

import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import kotlinx.serialization.json.*
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlin.text.Charsets.UTF_8

class PullRequestService() {
    private val httpClient = HttpClient.newBuilder().build()
    private val logger = LoggerFactory.getLogger(PullRequestService::class.java)

    fun getPullRequests(githubToken: String, repositoryInfo: Pair<String, String>): List<PullRequestInfo> {
        val targetRepoName = repositoryInfo.second.replace(".git", "")
        val url = "https://api.github.com/repos/${repositoryInfo.first}/$targetRepoName/pulls"

        val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "token $githubToken")
                .header("Accept", "application/vnd.github.v3+json")
                .build()

        try {
            val response = httpClient.send(request, BodyHandlers.ofString(UTF_8))
            return if (response.statusCode() == 200) {
                val jsonPullRequestRes = Json.parseToJsonElement(response.body()).jsonArray
                val pullRequests = mutableListOf<PullRequestInfo>()
                for (i in 0 until jsonPullRequestRes.size) {
                    val jsonPullRequest = jsonPullRequestRes[i].jsonObject
                    pullRequests.add(generatePullRequest(jsonPullRequest))
                }
                pullRequests
            } else {
                logger.error("Failed to fetch pull requests. Status code: ${response.statusCode()}")
                emptyList()
            }
        } catch (e: Exception) {
            logger.error("Error fetching pull requests: ", e)
            return emptyList()
        }
    }

    private fun generatePullRequest(jsonPullRequest: JsonObject): PullRequestInfo {
        return PullRequestInfo(
                name = jsonPullRequest["title"].toString(),
                requestUserId = jsonPullRequest["user"]?.jsonObject?.get("login").toString(),
                status = jsonPullRequest["state"].toString(),
                url = jsonPullRequest["url"].toString(),
                createdAt = jsonPullRequest["created_at"].toString(),
                updatedAt = jsonPullRequest["updated_at"]?.toString()
        )
    }
}