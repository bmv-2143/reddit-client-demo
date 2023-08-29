package com.example.finalattestationreddit.data.model.dto.post

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostListingData(
    val children: List<PostData>,
    val after: String?,
    val before: String?
)