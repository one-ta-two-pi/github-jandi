package com.onetatwopi.jandi.layout.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class IssueDetailInfo(
    val title: String,
    val body: String,
    val number: Integer,
    @SerializedName("user")
    @JsonAdapter(UsernameDeserializer::class)
    val createUserId: String,
    @SerializedName("closed_by")
    @JsonAdapter(UsernameDeserializer::class)
    val closedUserId: String,
    @SerializedName("html_url") val url: String,
    @SerializedName("state") val status: String,
    @SerializedName("created_at") val openAt: String,
    @SerializedName("closed_at") val closeAt: String,
) {
    val openAtAsDate: LocalDateTime
        get() {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            return LocalDateTime.parse(openAt, formatter)
        }
}
