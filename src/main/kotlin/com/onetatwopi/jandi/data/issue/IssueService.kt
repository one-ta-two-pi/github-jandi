package com.onetatwopi.jandi.data.issue

import com.onetatwopi.jandi.layout.dto.IssueInfo
import kotlinx.serialization.json.*

class IssueService {
    val mockIssue = "[{\"url\":\"https://api.github.com/repos/graceful-martin/hanghae99/issues/17\",\"repository_url\":\"https://api.github.com/repos/graceful-martin/hanghae99\",\"labels_url\":\"https://api.github.com/repos/graceful-martin/hanghae99/issues/17/labels{/name}\",\"comments_url\":\"https://api.github.com/repos/graceful-martin/hanghae99/issues/17/comments\",\"events_url\":\"https://api.github.com/repos/graceful-martin/hanghae99/issues/17/events\",\"html_url\":\"https://github.com/graceful-martin/hanghae99/issues/17\",\"id\":2116919822,\"node_id\":\"I_kwDOK1ejls5-LaIO\",\"number\":17,\"title\":\"Test\",\"user\":{\"login\":\"graceful-martin\",\"id\":56020202,\"node_id\":\"MDQ6VXNlcjU2MDIwMjAy\",\"avatar_url\":\"https://avatars.githubusercontent.com/u/56020202?v=4\",\"gravatar_id\":\"\",\"url\":\"https://api.github.com/users/graceful-martin\",\"html_url\":\"https://github.com/graceful-martin\",\"followers_url\":\"https://api.github.com/users/graceful-martin/followers\",\"following_url\":\"https://api.github.com/users/graceful-martin/following{/other_user}\",\"gists_url\":\"https://api.github.com/users/graceful-martin/gists{/gist_id}\",\"starred_url\":\"https://api.github.com/users/graceful-martin/starred{/owner}{/repo}\",\"subscriptions_url\":\"https://api.github.com/users/graceful-martin/subscriptions\",\"organizations_url\":\"https://api.github.com/users/graceful-martin/orgs\",\"repos_url\":\"https://api.github.com/users/graceful-martin/repos\",\"events_url\":\"https://api.github.com/users/graceful-martin/events{/privacy}\",\"received_events_url\":\"https://api.github.com/users/graceful-martin/received_events\",\"type\":\"User\",\"site_admin\":false},\"labels\":[],\"state\":\"open\",\"locked\":false,\"assignee\":null,\"assignees\":[],\"milestone\":null,\"comments\":0,\"created_at\":\"2024-02-04T07:10:47Z\",\"updated_at\":\"2024-02-04T07:10:47Z\",\"closed_at\":null,\"author_association\":\"OWNER\",\"active_lock_reason\":null,\"body\":\"test\",\"reactions\":{\"url\":\"https://api.github.com/repos/graceful-martin/hanghae99/issues/17/reactions\",\"total_count\":0,\"+1\":0,\"-1\":0,\"laugh\":0,\"hooray\":0,\"confused\":0,\"heart\":0,\"rocket\":0,\"eyes\":0},\"timeline_url\":\"https://api.github.com/repos/graceful-martin/hanghae99/issues/17/timeline\",\"performed_via_github_app\":null,\"state_reason\":null}]"

    fun getIssueList(): MutableList<IssueInfo> {
        val jsonIssues = Json.parseToJsonElement(mockIssue).jsonArray
        val issues = mutableListOf<IssueInfo>()
        for (i in 0 until jsonIssues.size) {
            val jsonIssue = jsonIssues[i].jsonObject
            issues.add(generateIssue(jsonIssue))
        }

        return issues
    }

    private fun generateIssue(jsonIssue: JsonObject) = IssueInfo(
        title = jsonIssue["title"]?.jsonPrimitive?.contentOrNull ?: "",
        createUserId = jsonIssue["user"]?.jsonObject?.get("login")?.jsonPrimitive?.contentOrNull ?: "",
        url = jsonIssue["url"]?.jsonPrimitive?.contentOrNull ?: "",
        status = jsonIssue["state"]?.jsonPrimitive?.contentOrNull ?: "",
        openAt = jsonIssue["created_at"]?.jsonPrimitive?.contentOrNull ?: "",
        closeAt = jsonIssue["closed_at"]?.jsonPrimitive?.contentOrNull ?: ""
    )
}