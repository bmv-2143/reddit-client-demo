package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.config.PagingConfig.PAGE_SIZE
import com.example.finalattestationreddit.data.data_sources.RedditNetworkDataSource
import com.example.finalattestationreddit.data.model.dto.post.PostData
import com.example.finalattestationreddit.data.model.dto.post.PostListingData

class GetAllRecentPostsPagingSource(
    private val redditNetworkDataSource: RedditNetworkDataSource
) : BasePagingSource<PostData>() {

    override suspend fun loadData(params: LoadParams<String>):
            LoadDataResult<PostData> {

        val after = params.key ?: CURSOR_FIRST_PAGE

        val responseData: PostListingData =
            redditNetworkDataSource.getAllRecentPosts(after, PAGE_SIZE)

        return LoadDataResult(
            responseData.toPostDataList(),
            responseData.before,
            responseData.after
        )
    }

}
