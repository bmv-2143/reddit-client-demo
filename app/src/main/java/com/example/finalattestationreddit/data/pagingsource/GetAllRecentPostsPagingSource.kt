package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.config.PagingConfig.PAGE_SIZE
import com.example.finalattestationreddit.data.data_sources.RedditNetworkDataSource
import com.example.finalattestationreddit.data.model.dto.post.PostData
import com.example.finalattestationreddit.data.model.dto.post.PostListingData

class GetAllRecentPostsPagingSource(
    private val redditNetworkDataSource: RedditNetworkDataSource
) : BasePagingSource<PostData>() {

    override suspend fun loadData(pageToLoadKey : String):
            LoadDataResult<PostData> {

        val responseData: PostListingData =
            redditNetworkDataSource.getAllRecentPosts(pageToLoadKey, PAGE_SIZE)

        return LoadDataResult(
            responseData.toPostDataList(),
            responseData.before,
            responseData.after
        )
    }

}
