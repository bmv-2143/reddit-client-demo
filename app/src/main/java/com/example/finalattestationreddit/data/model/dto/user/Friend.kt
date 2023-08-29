package com.example.finalattestationreddit.data.model.dto.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Friend(
    val name: String,
    val id: String,
    @Json(name = "created_utc") val createdUtc: Long?,
    val note: String?
)