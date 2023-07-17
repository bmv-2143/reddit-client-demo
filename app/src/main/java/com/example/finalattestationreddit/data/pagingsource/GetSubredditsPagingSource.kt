package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.PAGE_SIZE
import com.example.finalattestationreddit.data.RedditNetworkDataSource
import com.example.finalattestationreddit.data.dto.ListingData
import com.example.finalattestationreddit.data.dto.SubredditData

class GetSubredditsPagingSource(
    private val redditNetworkDataSource: RedditNetworkDataSource
) : BasePagingSource<SubredditData>() {

    override suspend fun loadData(params: LoadParams<String>):
            Pair<List<SubredditData>, Map<String, String?>> {

        val after = params.key ?: "" // todo: ???? add constant

        val responseData: ListingData = redditNetworkDataSource.getNewSubreddits(after, PAGE_SIZE)

        val beforeAfterMap = mapOf(
            CURSOR_BEFORE to responseData.before,
            CURSOR_AFTER to responseData.after
        )

        return Pair(responseData.toSubredditDataList(), beforeAfterMap)
    }

}

// todo: should be in dto?
fun ListingData.toSubredditDataList(): List<SubredditData> =
    this.children.map { it.data }