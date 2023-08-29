package com.example.finalattestationreddit.data.model.dto.subreddit

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditResponse(

    val kind: String,
    val data: SubredditData

)