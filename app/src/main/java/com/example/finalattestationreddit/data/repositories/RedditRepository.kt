package com.example.finalattestationreddit.data.repositories

import android.util.Log
import androidx.paging.PagingData
import com.example.finalattestationreddit.data.data_sources.RedditNetworkDataSource
import com.example.finalattestationreddit.data.model.dto.comment.Comment
import com.example.finalattestationreddit.data.model.dto.post.Post
import com.example.finalattestationreddit.data.model.dto.post.PostData
import com.example.finalattestationreddit.data.model.dto.post.PostListingData
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.model.dto.user.Friend
import com.example.finalattestationreddit.data.model.dto.user.User
import com.example.finalattestationreddit.data.model.errors.NetworkError
import com.example.finalattestationreddit.data.model.subscription.SubscriptionUpdateResult
import com.example.finalattestationreddit.data.pagingsource.BasePagingSource
import com.example.finalattestationreddit.log.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditRepository @Inject constructor(
    private val redditNetworkDataSource: RedditNetworkDataSource,
    private val tokenManager: TokenManager,
    private val pagerFactory: PagerFactory
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

    internal fun getSubreddits(subredditsListType: String): Flow<PagingData<SubredditData>> =
        pagerFactory.makeGetSubredditsPager(subredditsListType).flow

    internal suspend fun updateSubscription(
        subredditName: String,
        action: String
    ): SubscriptionUpdateResult {
        val updateSuccess = redditNetworkDataSource.updateSubscription(subredditName, action)
        return SubscriptionUpdateResult(subredditName, updateSuccess)
    }

    internal fun getSubscribedSubreddits(): Flow<PagingData<SubredditData>> =
        pagerFactory.makeSubscribedSubredditsPager().flow

    internal fun searchSubreddits(query: String): Flow<PagingData<SubredditData>> =
        pagerFactory.makeSearchSubredditsPager(query).flow

    internal fun getPosts(subredditDisplayName: String): Flow<PagingData<PostData>> =
        pagerFactory.makePostsPager(subredditDisplayName).flow

    internal fun getAllRecentPosts(): Flow<PagingData<PostData>> =
        pagerFactory.makeAllRecentPostsPager().flow

    internal suspend fun getSubreddit(subredditDisplayName: String): SubredditData? {
        return redditNetworkDataSource.getSubreddit(subredditDisplayName)
    }

    /**
     * @param targetId - post or comment id, which is base36 string
     */
    internal suspend fun vote(targetId: String, dir: Int) {
        redditNetworkDataSource.vote(targetId, dir)
    }

    internal suspend fun getFirstPost(postName: String): Post? =
        redditNetworkDataSource.getPostsById(postName).firstOrNull().let { it?.data }

    internal suspend fun getComment(commentName: String): Comment? =
        redditNetworkDataSource.getCommentById(commentName).firstOrNull().let { it?.data }

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

    internal suspend fun getUser(userName: String): User? =
        redditNetworkDataSource.getUser(userName)

    private suspend fun getMyName(): String? = redditNetworkDataSource.getMyUser()?.name

    internal suspend fun getMyUser(): User? = redditNetworkDataSource.getMyUser()

    internal suspend fun getUserPostsCount(username: String): Int =
        redditNetworkDataSource.getUserPostsAll(username).count()

    internal fun getUserPosts(username: String): Flow<PagingData<PostData>> =
        pagerFactory.makeUserPostsPager(username).flow

    internal fun getMySavedPosts(): Flow<PagingData<PostData>> = flow {
        val username = getMyName()

        if (username == null) {
            Log.e(TAG, "${::getMySavedPosts.name}: my username is null")
            emitAll(flowOf())
        } else {
            val pagerFlow = pagerFactory.makeMySavedPostsPager(username).flow
            emitAll(pagerFlow)
        }
    }

    internal suspend fun clearSavedPosts(): Boolean {
        val myUsername = getMyName()

        if (myUsername == null) {
            Log.e(TAG, "${::clearSavedPosts.name}: my username is null")
            return false
        }

        return unsaveSavedPosts(myUsername)
    }

    private suspend fun unsaveSavedPosts(myUsername: String): Boolean {
        val savedPosts = redditNetworkDataSource.getMySavedPosts(
            myUsername,
            BasePagingSource.CURSOR_FIRST_PAGE,
            10_000  // todo: hardcoded consts
        )

        if (savedPosts.children.isEmpty())
            return true

        return unsaveAll(savedPosts)
    }

    private suspend fun unsaveAll(savedPosts: PostListingData): Boolean {
        for (post in savedPosts.children) {
            val unsavePostSuccess = redditNetworkDataSource
                .setPostSavedState(post.data.name, false)

            if (!unsavePostSuccess)
                return false

            Log.e(TAG, "Unsaving post: ${post.data.name} done")
        }
        return true
    }

    internal suspend fun addFriend(username: String): Boolean =
        redditNetworkDataSource.addFriend(username)

    internal suspend fun removeFriend(username: String): Boolean =
        redditNetworkDataSource.removeFriend(username)

    internal suspend fun isFriend(username: String): Boolean =
        redditNetworkDataSource.getFriends().any { it.name == username }

    internal suspend fun setSavePostState(postName: String, isSaved: Boolean): Boolean {
        return redditNetworkDataSource.setPostSavedState(postName, isSaved)
    }

    internal suspend fun getFriends() : List<Friend> =
        redditNetworkDataSource.getFriends()
}