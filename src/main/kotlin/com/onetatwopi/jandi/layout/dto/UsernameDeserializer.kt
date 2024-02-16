package com.onetatwopi.jandi.layout.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UsernameDeserializer : JsonDeserializer<String> {
    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): String {
        return json.asJsonObject.get("login").asString
    }
}

