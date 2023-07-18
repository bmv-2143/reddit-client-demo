package com.example.finalattestationreddit.data.dto.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostListingData(
    @Json(name = "children") val children: List<PostData>,
    @Json(name = "after") val after: String?,
    @Json(name = "before") val before: String?
)