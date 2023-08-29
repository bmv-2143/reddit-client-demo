package com.example.finalattestationreddit.data.model.dto.subreddit

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditListingData(

    val children: List<SubredditResponse>,
    val before: String?,
    val after: String?

)