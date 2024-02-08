package com.onetatwopi.jandi.data.pr

import kotlinx.serialization.Serializable

@Serializable
data class PullRequestRequest(
        val repoOwner: String,
        val repoName: String,
        val githubToken: String
)

@Serializable
data class PullRequestResponse(
        val url: String,
        val title: String,
        val html_url: String,
        val state: String,
        val locked: Boolean,
        val created_at: String,
        val updated_at: String,
        val closed_at: String?,
        val merged_at: String?
)