package com.example.finalattestationreddit.data.dto.subreddit

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SubredditListingData(

    @Json(name="children") val children: List<SubredditResponse>,
    @Json(name="before") val before: String?,
    @Json(name="after") val after: String?

)