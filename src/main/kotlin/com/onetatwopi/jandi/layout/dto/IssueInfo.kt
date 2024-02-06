package com.onetatwopi.jandi.layout.dto

import java.time.LocalDateTime

data class IssueInfo(
    val title: String,
    val createUserId: String,
    val url: String,
    val status: String,
    val openAt: LocalDateTime,
    val closeAt: LocalDateTime?
) {
    constructor(
        title: String,
        createUserId: String,
        url: String,
        status: String,
        openAt: LocalDateTime
    ) : this(
        title, createUserId, url, status, openAt, null
    )
}