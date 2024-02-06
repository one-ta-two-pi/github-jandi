package com.onetatwopi.jandi.layout.dto

import java.time.LocalDateTime

data class PullRequestInfo(
    val name: String,
    val requestUserId: String,
    val status: String,
    val url: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
) {
    constructor(
        name: String,
        requestUserId: String,
        status: String,
        url: String,
        createdAt: LocalDateTime
    ) : this(
        name, requestUserId, status, url, createdAt, null
    )
}