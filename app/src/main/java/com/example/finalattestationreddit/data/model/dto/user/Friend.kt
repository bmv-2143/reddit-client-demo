package com.example.finalattestationreddit.data.model.dto.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Friend(
    @Json(name = "name") val name: String,
    @Json(name = "id") val id: String,
    @Json(name = "created_utc") val createdUtc: Long?,
    @Json(name = "note") val note: String?
)