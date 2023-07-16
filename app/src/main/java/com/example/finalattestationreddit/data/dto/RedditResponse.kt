package com.example.finalattestationreddit.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListingResponse(

    // todo: add fields annotations
    val kind: String,
    val data: ListingData
)

@JsonClass(generateAdapter = true)
data class ListingData(

    // todo: add fields annotations
    val children: List<SubredditResponse>
)

@JsonClass(generateAdapter = true)
data class SubredditResponse(

    // todo: add fields annotations
    val kind: String,
    val data: SubredditData
)

@JsonClass(generateAdapter = true)
data class SubredditData(

    // todo: add fields annotations
    val title: String,
    val display_name_prefixed: String,
    val subscribers: Int,
    val public_description: String
)
