package com.onetatwopi.jandi.layout.dto

data class IssueInfo(
    val title: String,
    val createUserId: String,
    val url: String,
    val status: String,
    val openAt: String,
    val closeAt: String?
)