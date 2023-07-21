package com.example.finalattestationreddit.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.finalattestationreddit.data.dto.post.PostData
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.pagingsource.GetSubredditPostsPagingSource
import com.example.finalattestationreddit.data.pagingsource.GetSubredditsPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE = 30
const val PREFETCH_DISTANCE = PAGE_SIZE / 3

@Singleton
class RedditRepository @Inject constructor(
    private val redditNetworkDataSource: RedditNetworkDataSource,
    private val tokenManager : TokenManager
) {

    val networkErrorsFlow = redditNetworkDataSource.networkErrorsFlow.onEach { event ->
        val networkError = event.peekContent()
        if (event.hasBeenConsumed) {
            return@onEach
        }
        if (networkError is NetworkError.Unauthorized) {
            removeAccessTokenSync()
        }
    }

    fun hasAccessToken(): Boolean = tokenManager.hasAccessToken()

    internal fun saveAccessToken(accessToken: String) =
        tokenManager.saveAccessToken(accessToken)

    internal fun removeAccessTokenSync() = tokenManager.removeAccessTokenSync()

//    internal fun getNewSubreddits(): Flow<PagingData<SubredditData>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = PAGE_SIZE,
//                prefetchDistance = PREFETCH_DISTANCE,
//                initialLoadSize = PAGE_SIZE
//            ),
//            pagingSourceFactory = { GetSubredditsPagingSource(redditNetworkDataSource) }
//        ).flow
//    }
//
//    internal fun getPopularSubreddits(): Flow<PagingData<SubredditData>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = PAGE_SIZE,
//                prefetchDistance = PREFETCH_DISTANCE,
//                initialLoadSize = PAGE_SIZE
//            ),
//            pagingSourceFactory = { GetSubredditsPagingSource(redditNetworkDataSource) }
//        ).flow
//    }

    internal fun getSubreddits(subredditsListType: String): Flow<PagingData<SubredditData>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                GetSubredditsPagingSource(
                    subredditsListType,
                    redditNetworkDataSource
                )
            }
        ).flow
    }

    internal suspend fun updateSubscription(subredditName: String, action: String): SubscriptionUpdateResult {
        val updateSuccess = redditNetworkDataSource.updateSubscription(subredditName, action)
        return SubscriptionUpdateResult(subredditName, updateSuccess)
    }

    internal fun getPosts(subredditDisplayName: String): Flow<PagingData<PostData>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                GetSubredditPostsPagingSource(
                    subredditDisplayName,
                    redditNetworkDataSource
                )
            }
        ).flow
    }

}