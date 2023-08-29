package com.example.finalattestationreddit.data.model.dto.comment

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentListingResponse(
    val data: CommentListingData
)