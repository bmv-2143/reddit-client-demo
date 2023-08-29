package com.example.finalattestationreddit.data.model.dto.comment

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(
    val name: String,
    val body: String?,
    val author: String?,
    @Json(name = "created_utc") val createdUtc: Long?,
    val kind: String?,
    val count: Int?,
    val children: List<String>?,
    val likedByUser: Boolean?,
    val score: Int?,
)