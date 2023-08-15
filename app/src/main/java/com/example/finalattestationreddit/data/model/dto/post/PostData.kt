package com.example.finalattestationreddit.data.model.dto.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostData(
    @Json(name = "data") val data: Post
)