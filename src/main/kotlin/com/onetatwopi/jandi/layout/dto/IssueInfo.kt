package com.onetatwopi.jandi.layout.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class IssueInfo(
    val title: String,
    @SerializedName("user")
    @JsonAdapter(UsernameDeserializer::class)
    val createUserId: String,
    val url: String,
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

class UsernameDeserializer : JsonDeserializer<String> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): String {
        return json.asJsonObject.get("login").asString
    }
}

