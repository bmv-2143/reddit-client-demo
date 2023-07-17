package com.example.finalattestationreddit.data

import com.example.finalattestationreddit.data.dto.ListingData
import com.example.finalattestationreddit.data.dto.ListingResponse
import com.example.finalattestationreddit.data.dto.SubredditData
import com.example.finalattestationreddit.data.pagingsource.toSubredditDataList
import javax.inject.Inject

class RedditNetworkDataSource @Inject constructor(private val redditService: RedditService) {

    suspend fun getNewSubreddits(after : String, perPage : Int): ListingData =
        redditService.redditApi.getNewSubreddits(after, perPage).data

    suspend fun getPopularSubreddits(): List<SubredditData> =
        redditService.redditApi.getPopularSubreddits().data.children.map { it.data }
}