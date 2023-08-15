package com.example.finalattestationreddit.data.model.dto.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "name") val name: String,
    @Json(name = "id") val id: String,
    @Json(name = "created_utc") val createdUtc: Long,
    @Json(name = "total_karma") val totalKarma: Int,
    @Json(name = "comment_karma") val commentKarma: Int,
    @Json(name = "icon_img") val iconImg: String?,
    @Json(name = "num_friends") val friendsNum: Int?,
)