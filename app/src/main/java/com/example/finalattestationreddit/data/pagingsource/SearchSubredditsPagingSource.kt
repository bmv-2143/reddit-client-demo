package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.config.PagingConfig.PAGE_SIZE
import com.example.finalattestationreddit.data.data_sources.RedditNetworkDataSource
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditListingData
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData

class SearchSubredditsPagingSource(
    private val query: String,
    private val redditNetworkDataSource: RedditNetworkDataSource
) : BasePagingSource<SubredditData>() {

    override suspend fun loadData(pageToLoadKey : String):
            LoadDataResult<SubredditData> {

        val responseData: SubredditListingData =
            redditNetworkDataSource.searchSubreddits(query, pageToLoadKey, PAGE_SIZE)

        return LoadDataResult(
            responseData.toSubredditDataList(),
            responseData.before,
            responseData.after
        )
    }

}
