package com.onetatwopi.jandi.data.issue

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.intellij.util.net.HTTPMethod
import com.onetatwopi.jandi.client.Category
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.layout.dto.IssueInfo

object IssueService {

    private const val MAX_ISSUES = 10

    init {
        parseIssueList()
    }

    private fun getResponse(): String = GitClient.repoRequest(
        method = HTTPMethod.GET,
        repo = "hanghae99",
        category = Category.ISSUE,
    )

    fun parseIssueList(): MutableList<IssueInfo> {
        val response = getResponse()
        val listType = object : TypeToken<List<IssueInfo>>() {}.type
        val jsonIssues: List<IssueInfo> = Gson().fromJson(response, listType)

        // TODO: 페이징 처리 미구현 대신 최대 10개만 가져오도록 처리
        val sortedIssues = jsonIssues.sortedByDescending { it.openAtAsDate }
        val limitedIssues = sortedIssues.take(MAX_ISSUES)

        return limitedIssues.toMutableList()
    }
}