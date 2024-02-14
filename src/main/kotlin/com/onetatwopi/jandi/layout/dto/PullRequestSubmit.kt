package com.onetatwopi.jandi.layout.dto

data class PullRequestSubmit(
    val title: String,
    val body: String?,
    val head: String,
    val base: String
)