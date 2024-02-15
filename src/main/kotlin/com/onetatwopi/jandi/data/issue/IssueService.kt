package com.onetatwopi.jandi.data.issue

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.intellij.util.net.HTTPMethod
import com.onetatwopi.jandi.client.Category
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.layout.dto.IssueInfo

object IssueService {

    private val MAX_ISSUES = 10

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
        return jsonIssues.toMutableList()
    }
}