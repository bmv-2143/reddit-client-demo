package com.example.finalattestationreddit.data.model.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendsData(
    val children: List<Friend>
)