package com.onetatwopi.jandi.layout.dto

data class PullRequestInfo(
        val name: String, // title
        val requestUserId: String,
        val status: String,
        val url: String,
        val createdAt: String,
        val updatedAt: String?
)
