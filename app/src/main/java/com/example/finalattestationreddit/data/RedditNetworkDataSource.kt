package com.example.finalattestationreddit.data

import com.example.finalattestationreddit.data.dto.SubredditData
import javax.inject.Inject

class RedditNetworkDataSource @Inject constructor(private val redditService: RedditService) {

    suspend fun getNewSubreddits(): List<SubredditData> {
        return redditService.redditApi.getNewSubreddits().data.children.map { it.data }
    }

    suspend fun getPopularSubreddits(): List<SubredditData> {
        return redditService.redditApi.getPopularSubreddits().data.children.map { it.data }
    }
}