package com.example.finalattestationreddit.data.dto.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostImage(
    @Json(name = "source") val source: PostSource
)