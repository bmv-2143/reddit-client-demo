package com.example.finalattestationreddit.data.model.dto.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddFriendRequest(
    @Json(name = "name") val name: String
)