package com.onetatwopi.jandi.layout.dto

data class PullRequestInfo(
    val title: String, // title
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