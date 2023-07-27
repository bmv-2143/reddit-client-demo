package com.example.finalattestationreddit.data.dto.comment

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentData(
    @Json(name = "data") val data: Comment
)