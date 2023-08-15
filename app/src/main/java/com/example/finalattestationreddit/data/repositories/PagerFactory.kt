package com.example.finalattestationreddit.data.repositories

import androidx.paging.Pager
import com.example.finalattestationreddit.data.config.PagingConfig
import com.example.finalattestationreddit.data.data_sources.RedditNetworkDataSource
import com.example.finalattestationreddit.data.model.dto.post.PostData
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.pagingsource.GetAllRecentPostsPagingSource
import com.example.finalattestationreddit.data.pagingsource.GetMySavedPostsPagingSource
import com.example.finalattestationreddit.data.pagingsource.GetSubredditPostsPagingSource
import com.example.finalattestationreddit.data.pagingsource.GetSubredditsPagingSource
import com.example.finalattestationreddit.data.pagingsource.GetSubscribedSubredditsPagingSource
import com.example.finalattestationreddit.data.pagingsource.GetUserPostsPagingSource
import com.example.finalattestationreddit.data.pagingsource.SearchSubredditsPagingSource
import javax.inject.Inject

class PagerFactory @Inject constructor(
    private val redditNetworkDataSource: RedditNetworkDataSource
) {

    internal fun makeGetSubredditsPager(subredditsListType: String): Pager<String, SubredditData> {
        return Pager(
            config = makePagingConfig(),
            pagingSourceFactory = {
                GetSubredditsPagingSource(
                    subredditsListType,
                    redditNetworkDataSource
                )
            }
        )
    }

    internal fun makeSubscribedSubredditsPager(): Pager<String, SubredditData> {
        return Pager(
            config = makePagingConfig(),
            pagingSourceFactory = {
                GetSubscribedSubredditsPagingSource(redditNetworkDataSource)
            }
        )
    }

    internal fun makeSearchSubredditsPager(query: String): Pager<String, SubredditData> {
        return Pager(
            config = makePagingConfig(),
            pagingSourceFactory = {
                SearchSubredditsPagingSource(
                    query,
                    redditNetworkDataSource
                )
            }
        )
    }

    internal fun makePostsPager(subredditName: String): Pager<String, PostData> {
        return Pager(
            config = makePagingConfig(),
            pagingSourceFactory = {
                GetSubredditPostsPagingSource(
                    subredditName,
                    redditNetworkDataSource
                )
            }
        )
    }

    internal fun makeAllRecentPostsPager(): Pager<String, PostData> {
        return Pager(
            config = makePagingConfig(),
            pagingSourceFactory = {
                GetAllRecentPostsPagingSource(redditNetworkDataSource)
            }
        )
    }

    internal fun makeUserPostsPager(username: String): Pager<String, PostData> {
        return Pager(
            config = makePagingConfig(),
            pagingSourceFactory = {
                GetUserPostsPagingSource(username, redditNetworkDataSource)
            }
        )
    }

    internal fun makeMySavedPostsPager(username: String): Pager<String, PostData> {
        return Pager(
            config = makePagingConfig(),
            pagingSourceFactory = {
                GetMySavedPostsPagingSource(
                    username,
                    redditNetworkDataSource
                )
            }
        )
    }


    private fun makePagingConfig() = androidx.paging.PagingConfig(
        pageSize = PagingConfig.PAGE_SIZE,
        prefetchDistance = PagingConfig.PREFETCH_DISTANCE,
        initialLoadSize = PagingConfig.PAGE_SIZE
    )

}