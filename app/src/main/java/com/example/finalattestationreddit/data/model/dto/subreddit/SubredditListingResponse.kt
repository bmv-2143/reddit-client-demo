package com.example.finalattestationreddit.data.model.dto.subreddit

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditListingResponse(

    val kind: String,
    val data: SubredditListingData

)

