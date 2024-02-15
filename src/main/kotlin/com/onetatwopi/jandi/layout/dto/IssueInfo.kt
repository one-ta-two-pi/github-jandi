package com.onetatwopi.jandi.layout.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class IssueInfo(
    val title: String,
    @SerializedName("user")
    @JsonAdapter(UsernameDeserializer::class)
    val createUserId: String,
    val url: String,
    @SerializedName("state") val status: String,
    @SerializedName("created_at") val openAt: String,
    @SerializedName("closed_at") val closeAt: String?
)

class UsernameDeserializer : JsonDeserializer<String> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): String {
        return json.asJsonObject.get("login").asString
    }
}

