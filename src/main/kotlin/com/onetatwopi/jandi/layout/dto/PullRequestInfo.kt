package com.onetatwopi.jandi.layout.dto

data class PullRequestInfo(
    val title: String,
    val requestUserId: String,
    val status: String,
    val url: String,
    val createdAt: String,
    val updatedAt: String?,
) {
    constructor(
        title: String,
        requestUserId: String,
        status: String,
        url: String,
        createdAt: String,
    ) : this(title, requestUserId, status, url, createdAt, null)
}

data class PullRequestDetailInfo(
        val number: Int,
        val title: String,
        val requestUserId: String,
        val status: String,
        val url: String,
        val body: String?,
        val createdAt: String,
        val updatedAt: String?,
        val closedAt: String?,
        val mergedAt: String?,
        val commitUrl: String?,
        val headBranch: String?,
        val baseBranch: String?,
) {
    constructor(
            number: Int,
            title: String,
            requestUserId: String,
            status: String,
            url: String,
            body: String,
            createdAt: String,
    ) : this(number, title, requestUserId, status, url, body, createdAt, null, null, null, null, null, null)
}