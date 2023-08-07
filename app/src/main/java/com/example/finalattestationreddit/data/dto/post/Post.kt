package com.example.finalattestationreddit.data.dto.post

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "name") val name: String,
    @Json(name = "title") val title: String,
    @Json(name = "author") val author: String,
    @Json(name = "created_utc") val createdUtc: Long,
    @Json(name = "num_comments") val numComments: Int,
    @Json(name = "permalink") val permalink: String,
    @Json(name = "thumbnail") val thumbnail: String,
    @Json(name = "url") val url: String,
    @Json(name = "preview") val preview: PostPreview?,
    @Json(name = "selftext") val selfText: String,
    @Json(name = "score") val score: Int,
    @Json(name = "likes") val likedByUser: Boolean?,
    @Json(name = "saved") val saved: Boolean
) {

    // todo: should it be here? in DTO? should it be defined as extension fun in another layer?
    fun getFirstUrlOrNull(): String? = preview?.images?.firstOrNull()?.source?.url

    fun getPostId() : String = name.substringAfter("_")
}