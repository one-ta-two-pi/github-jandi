package com.onetatwopi.jandi.layout.dto

import com.esotericsoftware.kryo.kryo5.serializers.TimeSerializers
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class PullRequestInfo(
        val name: String, // title
        val requestUserId: String,
        val status: String,
        val url: String,
        @Serializable(with = LocalDateTimeSerializer::class) val createdAt: String,
        @Serializable(with = LocalDateTimeSerializer::class) val updatedAt: String?
)

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_DATE_TIME

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val string = value.format(formatter)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string, formatter)
    }
}