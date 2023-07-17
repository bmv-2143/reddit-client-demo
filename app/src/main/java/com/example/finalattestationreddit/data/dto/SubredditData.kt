package com.example.finalattestationreddit.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditData(

    @Json(name="id") val id: String,
    @Json(name="title") val title: String,
    @Json(name="display_name") val displayName: String,
    @Json(name="display_name_prefixed") val displayNamePrefixed: String,
    @Json(name="subscribers") val subscribers: Int,
    @Json(name="public_description") val publicDescription: String

)