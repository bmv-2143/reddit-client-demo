package com.example.finalattestationreddit.data

import com.example.finalattestationreddit.data.dto.ListingData
import javax.inject.Inject

class RedditNetworkDataSource @Inject constructor(private val redditService: RedditService) {

    suspend fun getSubreddits(
        subredditsListType: String,
        after: String,
        perPage: Int
    ): ListingData =
        redditService.redditApi.getSubreddits(subredditsListType, after, perPage).data

}