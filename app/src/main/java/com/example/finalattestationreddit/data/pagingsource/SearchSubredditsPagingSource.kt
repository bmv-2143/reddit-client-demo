package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.repositories.PAGE_SIZE
import com.example.finalattestationreddit.data.RedditNetworkDataSource
import com.example.finalattestationreddit.data.dto.subreddit.SubredditListingData
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.mappers.toSubredditDataList

class SearchSubredditsPagingSource(
    private val query: String,
    private val redditNetworkDataSource: RedditNetworkDataSource
) : BasePagingSource<SubredditData>() {

    override suspend fun loadData(params: LoadParams<String>):
            Pair<List<SubredditData>, Map<String, String?>> {

        val after = params.key ?: CURSOR_FIRST_PAGE

        val responseData: SubredditListingData =
            redditNetworkDataSource.searchSubreddits(query, after, PAGE_SIZE)

        val beforeAfterMap = mapOf(
            CURSOR_BEFORE to responseData.before,
            CURSOR_AFTER to responseData.after
        )

        return Pair(responseData.toSubredditDataList(), beforeAfterMap)
    }

}
