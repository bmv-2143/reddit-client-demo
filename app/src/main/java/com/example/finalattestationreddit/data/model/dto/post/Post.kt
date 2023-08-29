package com.example.finalattestationreddit.data.model.dto.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
    val name: String,
    val title: String,
    val author: String,
    @Json(name = "created_utc") val createdUtc: Long,
    @Json(name = "num_comments") val numComments: Int,
    val permalink: String,
    val thumbnail: String,
    val url: String,
    val preview: PostPreview?,
    @Json(name = "selftext") val selfText: String,
    val score: Int,
    val likedByUser: Boolean?,
    val saved: Boolean
)