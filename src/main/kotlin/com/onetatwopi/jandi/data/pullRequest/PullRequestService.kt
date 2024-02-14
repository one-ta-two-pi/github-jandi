package com.onetatwopi.jandi.data.pullRequest

import com.onetatwopi.jandi.layout.dto.PullRequestDetailInfo
import com.onetatwopi.jandi.layout.dto.PullRequestInfo
import kotlinx.serialization.json.*
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.text.Charsets.UTF_8

class PullRequestService {
    private val httpClient = HttpClient.newBuilder().build()
    private val logger = LoggerFactory.getLogger(PullRequestService::class.java)

    fun getPullRequestList(githubToken: String, repositoryInfo: Pair<String, String>): List<PullRequestInfo> {
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
                logger.error("Failed to fetch pull request list. Status code: ${response.statusCode()}")
                emptyList()
            }
        } catch (e: Exception) {
            logger.error("Error fetching pull request list: ", e)
            return emptyList()
        }
    }

    fun getPullRequest(githubToken: String, repositoryInfo: Pair<String, String>, number: Int): PullRequestDetailInfo? {
        val targetRepoName = repositoryInfo.second.replace(".git", "")
        val url = "https://api.github.com/repos/${repositoryInfo.first}/$targetRepoName/pulls/$number"

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "token $githubToken")
            .header("Accept", "application/vnd.github.v3+json")
            .build()

        try {
            val response = httpClient.send(request, BodyHandlers.ofString(UTF_8))
            return if (response.statusCode() == 200) {
                val jsonPullRequestRes = Json.parseToJsonElement(response.body()).jsonObject
                println(generatePullRequestDetail(jsonPullRequestRes))
                return generatePullRequestDetail(jsonPullRequestRes)
            } else {
                logger.error("Failed to fetch pull request. Status code: ${response.statusCode()}")
                return null
            }
        } catch (e: Exception) {
            logger.error("Error fetching pull request: ", e)
            return null
        }
    }

    fun createPullRequest(githubToken: String, repositoryInfo: Pair<String, String>) {
        val targetRepoName = repositoryInfo.second.replace(".git", "")
        val url = "https://api.github.com/repos/${repositoryInfo.first}/$targetRepoName/pulls"

        // TODO: parameter should be fixed (check branch info)
        val prMockData = """
            {
              "title": "pr create test",
              "user": "username",
              "body": "Please check pr",
              "head": "${repositoryInfo.first}:develop",
              "base": "master"
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "token $githubToken")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .header("Accept", "application/vnd.github.v3+json")
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(prMockData))
            .build()

        try {
            val response = httpClient.send(request, BodyHandlers.ofString(UTF_8))
            return if (response.statusCode() == 201) {
                // TODO: check return type
            } else {
                logger.error("Failed to create pull request. Status code: ${response.statusCode()}")
            }
        } catch (e: IOException) {
            // TODO: check exception handling and logging
            logger.error("Network request error occurred: ${e.message}")
        } catch (e: IllegalArgumentException) {
            logger.error("Error in HttpRequest configuration: ${e.message}")
        } catch (e: Exception) {
            logger.error("An unknown exception has occurred: ${e.message}")
        }
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

    private fun generatePullRequestDetail(jsonPullRequest: JsonObject): PullRequestDetailInfo {
        val isoDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val customDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        val createdAt = convertDateString(jsonPullRequest["created_at"]?.jsonPrimitive?.content, isoDateTimeFormatter, customDateTimeFormatter)
        val updatedAt = convertDateString(jsonPullRequest["updated_at"]?.jsonPrimitive?.content, isoDateTimeFormatter, customDateTimeFormatter)
        val closedAt = convertDateString(jsonPullRequest["closed_at"]?.jsonPrimitive?.content, isoDateTimeFormatter, customDateTimeFormatter)
        val mergedAt = convertDateString(jsonPullRequest["merged_at"]?.jsonPrimitive?.content, isoDateTimeFormatter, customDateTimeFormatter)

        return PullRequestDetailInfo(
            number = jsonPullRequest["number"]?.jsonPrimitive?.content?.toInt() ?: 1,
            title = jsonPullRequest["title"]?.jsonPrimitive?.content ?: "",
            requestUserId = jsonPullRequest["user"]?.jsonObject?.get("login")?.jsonPrimitive?.content ?: "",
            status = jsonPullRequest["state"]?.jsonPrimitive?.content ?: "",
            url = jsonPullRequest["url"]?.jsonPrimitive?.content ?: "",
            body = jsonPullRequest["body"]?.jsonPrimitive?.content,
            createdAt = createdAt ?: "yyyy-MM-dd HH:mm:ss",
            updatedAt = updatedAt,
            closedAt = closedAt,
            mergedAt = mergedAt,
            commitUrl = jsonPullRequest["commits_url"]?.jsonPrimitive?.content,
            headBranch = jsonPullRequest["head"]?.jsonObject?.get("ref")?.jsonPrimitive?.content,
            baseBranch = jsonPullRequest["base"]?.jsonObject?.get("ref")?.jsonPrimitive?.content
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
            //    throw RuntimeException("Fail to convert local time.")
            return null
        }
    }
}