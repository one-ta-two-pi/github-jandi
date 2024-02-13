package com.onetatwopi.jandi.layout.dto

data class PullRequestInfo(
    val name: String, // title
    val requestUserId: String,
    val status: String,
    val url: String,
    val createdAt: String,
    val updatedAt: String?
) {
    constructor(
        name: String,
        requestUserId: String,
        status: String,
        url: String,
        createdAt: String
    ) : this(name, requestUserId, status, url, createdAt, null)
}