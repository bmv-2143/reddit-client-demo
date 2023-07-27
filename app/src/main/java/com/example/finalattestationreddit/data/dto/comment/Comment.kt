package com.example.finalattestationreddit.data.dto.comment

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(
    @Json(name = "name") val name: String,
    @Json(name = "body") val body: String?,
    @Json(name = "author") val author: String?,
    @Json(name = "created_utc") val createdUtc: Long?,
//    @Json(name = "replies") val replies: CommentListingResponse?,
    @Json(name = "kind") val kind: String?,
    @Json(name = "count") val count: Int?,
    @Json(name = "children") val children: List<String>?
)