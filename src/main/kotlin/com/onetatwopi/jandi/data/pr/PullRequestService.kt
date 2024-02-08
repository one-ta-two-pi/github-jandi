package com.onetatwopi.jandi.data.pr

import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import kotlin.text.Charsets.UTF_8

class PullRequestService(
        private val githubToken: String,
        private val repoOwner: String,
        private val repoName: String
) {
    private val httpClient = HttpClient.newBuilder().build()
    private val logger = LoggerFactory.getLogger(PullRequestService::class.java)

    fun fetchAndDisplayPullRequests(){
        val url = "https://api.github.com/repos/$repoOwner/$repoName/pulls"
        logger.info("Fetching pull requests from URL: $url")

        val request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "token $githubToken")
                .header("Accept", "application/vnd.github.v3+json")
                .build()

        try {
            val response = httpClient.send(request, BodyHandlers.ofString(UTF_8))
            if (response.statusCode() == 200) {
                val finalResponse = parsePullRequests(response)
//                return finalResponse
            } else {
                logger.error("Failed to fetch pull requests. Status code: ${response.statusCode()}")
            }
        } catch (e: Exception) {
            logger.error("Error fetching pull requests: ", e)
        }
    }

    private fun parsePullRequests(response: HttpResponse<String>): List<PullRequestInfo> {
        val json = Json { ignoreUnknownKeys = true }
        val pullRequestList: List<PullRequestInfo> = json.decodeFromString(response.body())
        return pullRequestList
    }
}