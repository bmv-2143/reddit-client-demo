package com.example.finalattestationreddit.data.model.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendsResponse(
    val data: FriendsData
)