package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.PAGE_SIZE
import com.example.finalattestationreddit.data.RedditNetworkDataSource
import com.example.finalattestationreddit.data.dto.subreddit.SubredditListingData
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.mappers.toSubredditDataList

class GetSubscribedSubredditsPagingSource(
    private val redditNetworkDataSource: RedditNetworkDataSource
) : BasePagingSource<SubredditData>() {

    override suspend fun loadData(params: LoadParams<String>):
            Pair<List<SubredditData>, Map<String, String?>> {

        val after = params.key ?: "" // todo: ???? add constant

        val responseData: SubredditListingData =
            redditNetworkDataSource.getSubscribedSubreddits(after, PAGE_SIZE)

        val beforeAfterMap = mapOf(
            CURSOR_BEFORE to responseData.before,
            CURSOR_AFTER to responseData.after
        )

        return Pair(responseData.toSubredditDataList(), beforeAfterMap)
    }

}