package com.onetatwopi.jandi.layout.dto

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class IssueInfo(
    val title: String,
    @SerializedName("user")
    @JsonAdapter(UsernameDeserializer::class)
    val createUserId: String,
    @SerializedName("html_url") val url: String,
    @SerializedName("state") val status: String,
    val number: Int,
    @SerializedName("created_at") val openAt: String,
    @SerializedName("closed_at") val closeAt: String,
) {
    val openAtAsDate: LocalDateTime
        get() {
            if (openAt == null) {
                return LocalDateTime.of(2000, 1, 1, 0, 0, 0)
            }

            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            return LocalDateTime.parse(openAt, formatter)
        }

    val openAtAsString: String
        get() {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return openAtAsDate.format(formatter)
        }

    val upperStatus: String
        get() = status.uppercase()

    val titleWithNumber: String
        get() = "$title #$number"
}