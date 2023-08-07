package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.PAGE_SIZE
import com.example.finalattestationreddit.data.RedditNetworkDataSource
import com.example.finalattestationreddit.data.dto.post.PostData
import com.example.finalattestationreddit.data.dto.post.PostListingData
import com.example.finalattestationreddit.data.mappers.toPostDataList

class GetUserPostsPagingSource(
    private val username: String,
    private val redditNetworkDataSource: RedditNetworkDataSource
) : BasePagingSource<PostData>() {

    override suspend fun loadData(params: LoadParams<String>):
            Pair<List<PostData>, Map<String, String?>> {

        val after = params.key ?: "" // todo: ???? add constant

        val responseData: PostListingData =
            redditNetworkDataSource.getUserPosts(username, after, PAGE_SIZE)

        val beforeAfterMap = mapOf(
            CURSOR_BEFORE to responseData.before,
            CURSOR_AFTER to responseData.after
        )

        return Pair(responseData.toPostDataList(), beforeAfterMap)
    }

}