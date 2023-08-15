package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.config.PagingConfig.PAGE_SIZE
import com.example.finalattestationreddit.data.data_sources.RedditNetworkDataSource
import com.example.finalattestationreddit.data.dto.post.PostData
import com.example.finalattestationreddit.data.dto.post.PostListingData
import com.example.finalattestationreddit.data.mappers.toPostDataList

class GetUserPostsPagingSource(
    private val username: String,
    private val redditNetworkDataSource: RedditNetworkDataSource
) : BasePagingSource<PostData>() {

    override suspend fun loadData(params: LoadParams<String>):
            Pair<List<PostData>, Map<String, String?>> {

        val after = params.key ?: CURSOR_FIRST_PAGE

        val responseData: PostListingData =
            redditNetworkDataSource.getUserPosts(username, after, PAGE_SIZE)

        val beforeAfterMap = mapOf(
            CURSOR_BEFORE to responseData.before,
            CURSOR_AFTER to responseData.after
        )

        return Pair(responseData.toPostDataList(), beforeAfterMap)
    }

}