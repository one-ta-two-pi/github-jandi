package com.onetatwopi.jandi.data.issue

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intellij.util.net.HTTPMethod
import com.onetatwopi.jandi.client.Category
import com.onetatwopi.jandi.client.GitClient
import com.onetatwopi.jandi.layout.dto.IssueDetailInfo
import com.onetatwopi.jandi.layout.dto.IssueInfo
import com.onetatwopi.jandi.layout.dto.IssueSubmit
import org.apache.http.message.BasicNameValuePair

class IssueService private constructor() {

    companion object {
        private const val MAX_ISSUES = 10

        val instance: IssueService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            IssueService()
        }
    }

    init {
        parseIssueList()
    }

    private fun getRepositoryIssues(): String {
        return GitClient.repoRequest(
            method = HTTPMethod.GET,
            repo = GitClient.repos[0],
            category = Category.ISSUE,
        )
    }

    private fun getRepositoryIssueDetail(number: Int): String = GitClient.repoRequest(
        method = HTTPMethod.GET,
        repo = GitClient.repos[0],
        category = Category.ISSUE,
        number = number
    )

    private fun createRepositoryIssue(issueSubmit: IssueSubmit): String = GitClient.repoRequest(
        method = HTTPMethod.POST,
        repo = GitClient.repos[0],
        category = Category.ISSUE,
        body = listOf(
            BasicNameValuePair("title", issueSubmit.title),
            BasicNameValuePair("body", issueSubmit.body),
        )

        // TODO: assignee, milestone, labels, assignees 미구현
    )

    fun parseIssueList(): MutableList<IssueInfo> {
        val response = getRepositoryIssues()
        val listType = object : TypeToken<List<IssueInfo>>() {}.type
        val jsonIssues: List<IssueInfo> = Gson().fromJson(response, listType)

        // TODO: 페이징 처리 미구현 대신 최대 10개만 가져오도록 처리
        val sortedIssues = jsonIssues.sortedByDescending { it.openAtAsDate }
        val limitedIssues = sortedIssues.take(MAX_ISSUES)

        return limitedIssues.toMutableList()
    }

    fun createIssue(issueSubmit: IssueSubmit) {
        createRepositoryIssue(issueSubmit)
    }

    fun getIssueDetail(number: Int): IssueDetailInfo {
        val response = getRepositoryIssueDetail(number)
        return Gson().fromJson(response, IssueDetailInfo::class.java)
    }
}