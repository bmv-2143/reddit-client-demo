package com.example.finalattestationreddit.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.data.dto.post.PostData
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.dto.user.User
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
    private val tokenManager: TokenManager
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

    internal fun removeAccessTokenSync() = tokenManager.removeAccessToken()

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

    internal suspend fun updateSubscription(
        subredditName: String,
        action: String
    ): SubscriptionUpdateResult {
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

    internal suspend fun getSubreddit(subredditDisplayName: String): SubredditData? {
        return redditNetworkDataSource.getSubreddit(subredditDisplayName)
    }

    /**
     * @param targetId - post or comment id, which is base36 string
     */
    internal suspend fun vote(targetId: String, dir: Int) {
        redditNetworkDataSource.vote(targetId, dir)
    }

    internal suspend fun getFirstPost(postName: String): Post? {
        return redditNetworkDataSource.getPostsById(postName).firstOrNull().let { it?.data }
    }

    internal suspend fun getComment(commentName: String): Comment? {
        return redditNetworkDataSource.getCommentById(commentName).firstOrNull().let { it?.data }
    }

    internal suspend fun getPostComments(
        subredditDisplayName: String,
        article: String,
        commentsCountLimit: Int
    ): List<Comment> {
        return redditNetworkDataSource.getPostComments(
            subredditDisplayName,
            article,
            limit = commentsCountLimit
        ).children.map { it.data }
    }

    internal suspend fun getUser(userName: String) : User? =
        redditNetworkDataSource.getUser(userName)

    internal suspend fun getUserPostsCount(username: String) : Int {
        return redditNetworkDataSource.getUserPosts(username).count()
    }

    internal suspend fun addFriend(username: String) : Boolean =
        redditNetworkDataSource.addFriend(username)

    internal suspend fun removeFriend(username: String) : Boolean =
        redditNetworkDataSource.removeFriend(username)

    internal suspend fun isFriend(username: String) : Boolean =
        redditNetworkDataSource.getFriends().any { it.name == username }
}