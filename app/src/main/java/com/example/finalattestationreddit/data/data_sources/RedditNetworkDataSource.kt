package com.example.finalattestationreddit.data.data_sources

import android.util.Log
import com.example.finalattestationreddit.data.model.dto.comment.CommentData
import com.example.finalattestationreddit.data.model.dto.comment.CommentListingData
import com.example.finalattestationreddit.data.model.dto.post.PostData
import com.example.finalattestationreddit.data.model.dto.post.PostListingData
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditListingData
import com.example.finalattestationreddit.data.model.dto.user.Friend
import com.example.finalattestationreddit.data.model.dto.user.User
import com.example.finalattestationreddit.data.model.errors.NetworkError
import com.example.finalattestationreddit.data.model.events.Event
import com.example.finalattestationreddit.data.network.RedditService
import com.example.finalattestationreddit.log.TAG
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditNetworkDataSource @Inject constructor(private val redditService: RedditService) {

    private val _networkErrorFlow = MutableSharedFlow<Event<NetworkError>>(replay = 1)
    val networkErrorsFlow = _networkErrorFlow.asSharedFlow()

    suspend fun getSubreddits(
        subredditsListType: String,
        after: String,
        perPage: Int
    ): SubredditListingData {

        return try {
            redditService.redditApi.getSubreddits(subredditsListType, after, perPage).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptySubredditListingData()
        } catch (e: HttpException) {
            handleHttpException(e, ::getSubreddits.name)
            emptySubredditListingData()
        } catch (e: Exception) {
            logError(::getSubreddits.name, e)
            emptySubredditListingData()
        }
    }

    private suspend fun handleUnknownHostError(e: UnknownHostException) {
        logError(::handleUnknownHostError.name, e)
        _networkErrorFlow.emit(
            Event(
                NetworkError.NoInternetConnection
            )
        )
    }

    private suspend fun handleHttpException(e: HttpException, methodName: String) {
        Log.e(TAG,
            "${::handleHttpException.name} error: method: ${methodName}, message: ${e.message}")
        when (e.code()) {
            403 -> _networkErrorFlow.emit(Event(NetworkError.ForbiddenApiRateExceeded))

            401 -> _networkErrorFlow.emit(Event(NetworkError.Unauthorized))

            else -> _networkErrorFlow.emit(Event(NetworkError.HttpError))
        }
    }

    private fun logError(methodName: String, e: Exception) {
        Log.e(TAG, "$methodName error: ${e.message}")
    }

    private fun emptySubredditListingData(): SubredditListingData =
        SubredditListingData(emptyList(), null, null)

    suspend fun getSubscribedSubreddits(
        after: String,
        perPage: Int
    ): SubredditListingData {
        return try {
            redditService.redditApi.getSubscribedSubreddits(after, perPage).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptySubredditListingData()
        } catch (e: HttpException) {
            handleHttpException(e, ::getSubscribedSubreddits.name)
            emptySubredditListingData()
        } catch (e: Exception) {
            logError(::getSubscribedSubreddits.name, e)
            emptySubredditListingData()
        }
    }

    suspend fun updateSubscription(subredditName: String, action: String): Boolean {
        return try {
            redditService.redditApi.updateSubscription(subredditName, action)
            true
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            false
        } catch (e: HttpException) {
            handleHttpException(e, ::updateSubscription.name)
            false
        } catch (e: Exception) {
            logError(::updateSubscription.name, e)
            false
        }
    }

    suspend fun searchSubreddits(
        query: String,
        after: String,
        perPage: Int
    ): SubredditListingData {
        return try {
            redditService.redditApi.searchSubreddits(query, after, perPage).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptySubredditListingData()
        } catch (e: HttpException) {
            handleHttpException(e, ::searchSubreddits.name)
            emptySubredditListingData()
        } catch (e: Exception) {
            logError(::searchSubreddits.name, e)
            emptySubredditListingData()
        }
    }

    suspend fun getSubredditPosts(
        subredditDisplayName: String,
        after: String,
        perPage: Int
    ): PostListingData {
        return try {
            redditService.redditApi.getSubredditPosts(subredditDisplayName, after, perPage).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyPostListingData()
        } catch (e: HttpException) {
            handleHttpException(e, ::getSubredditPosts.name)
            emptyPostListingData()
        } catch (e: Exception) {
            logError(::getSubredditPosts.name, e)
            emptyPostListingData()
        }
    }

    suspend fun getAllRecentPosts(
        after: String,
        perPage: Int
    ): PostListingData {
        return try {
            redditService.redditApi.getAllRecentPosts(after, perPage).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyPostListingData()
        } catch (e: HttpException) {
            handleHttpException(e, ::getAllRecentPosts.name)
            emptyPostListingData()
        } catch (e: Exception) {
            logError(::getAllRecentPosts.name, e)
            emptyPostListingData()
        }
    }

    private fun emptyPostListingData(): PostListingData =
        PostListingData(emptyList(), null, null)

    suspend fun getPostsById(postName: String): List<PostData> {
        return redditService.redditApi.getPostsById(postName).data.children
    }

    suspend fun getCommentById(commentName: String): List<CommentData> {
        return redditService.redditApi.getCommentById(commentName).data.children
    }

    suspend fun getSubreddit(subredditDisplayName: String): SubredditData? {
        return try {
            redditService.redditApi.getSubreddit(subredditDisplayName).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            null
        } catch (e: HttpException) {
            handleHttpException(e, ::getSubreddit.name)
            null
        } catch (e: Exception) {
            logError(::getSubreddit.name, e)
            null
        }
    }

    suspend fun vote(targetId: String, dir: Int): Boolean {
        return try {
            redditService.redditApi.vote(targetId, dir)
            true
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            false
        } catch (e: HttpException) {
            handleHttpException(e, ::vote.name)
            false
        } catch (e: Exception) {
            logError(::vote.name, e)
            false
        }
    }

    suspend fun getPostComments(
        subredditDisplayName: String,
        postName: String,
        limit: Int? = null,
    ): CommentListingData {
        return try {
            getOnlyCommentsToPost(
                subredditDisplayName,
                postName,
                limit = limit
            )
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyCommentListingData()
        } catch (e: HttpException) {
            handleHttpException(e, ::getPostComments.name)
            emptyCommentListingData()
        } catch (e: Exception) {
            logError(::getPostComments.name, e)
            emptyCommentListingData()
        }
    }

    private suspend fun getOnlyCommentsToPost(
        subredditDisplayName: String,
        postName: String,
        limit: Int?
    ): CommentListingData {
        val response =
            redditService.redditApi.getPostComments(
                subredditDisplayName,
                postName,
                limit = limit
            )
        return if (response.size >= 2) {
            response[1].data
        } else {
            emptyCommentListingData()
        }
    }

    internal suspend fun getUser(userName: String): User? {
        return try {
            redditService.redditApi.getUser(userName).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            null
        } catch (e: HttpException) {
            handleHttpException(e, ::getUser.name)
            null
        } catch (e: Exception) {
            logError(::getUser.name, e)
            null
        }
    }

    internal suspend fun getMyUser(): User? {
        return try {
            redditService.redditApi.getMe()
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            null
        } catch (e: HttpException) {
            handleHttpException(e, ::getMyUser.name)
            null
        } catch (e: Exception) {
            logError(::getMyUser.name, e)
            null
        }
    }

    internal suspend fun getUserPosts(
        username: String,
        after: String,
        perPage: Int
    ): PostListingData {
        return try {
            redditService.redditApi.getUserPosts(username, after, perPage).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyPostListingData()
        } catch (e: HttpException) {
            handleHttpException(e, ::getUserPosts.name)
            emptyPostListingData()
        } catch (e: Exception) {
            logError(::getUserPosts.name, e)
            emptyPostListingData()
        }
    }

    internal suspend fun getUserPostsAll(username: String): List<PostData> {
        return try {
            redditService.redditApi.getUserPostsAll(username).data.children
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyList()
        } catch (e: HttpException) {
            handleHttpException(e, ::getUserPostsAll.name)
            emptyList()
        } catch (e: Exception) {
            logError(::getUserPostsAll.name, e)
            emptyList()
        }
    }

    internal suspend fun getMySavedPosts(
        myUsername: String,
        after: String,
        pageSize: Int
    ): PostListingData {
        return try {
            redditService.redditApi.getSavedPosts(myUsername, after, pageSize).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyPostListingData()
        } catch (e: HttpException) {
            handleHttpException(e, ::getMySavedPosts.name)
            emptyPostListingData()
        } catch (e: Exception) {
            logError(::getMySavedPosts.name, e)
            emptyPostListingData()
        }
    }

    internal suspend fun addFriend(username: String): Boolean {
        return try {
            redditService.redditApi.addFriend(username)
            true
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            false
        } catch (e: HttpException) {
            handleHttpException(e, ::addFriend.name)
            false
        } catch (e: Exception) {
            logError(::addFriend.name, e)
            false
        }
    }

    internal suspend fun removeFriend(username: String): Boolean {
        return try {
            redditService.redditApi.removeFriend(username)
            true
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            false
        } catch (e: HttpException) {
            handleHttpException(e, ::removeFriend.name)
            false
        } catch (e: Exception) {
            logError(::removeFriend.name, e)
            false
        }
    }

    internal suspend fun getFriends(): List<Friend> {
        return try {
            redditService.redditApi.getFriends().data.children
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyList()
        } catch (e: HttpException) {
            handleHttpException(e, ::getFriends.name)
            emptyList()
        } catch (e: Exception) {
            logError(::getFriends.name, e)
            emptyList()
        }
    }

    internal suspend fun setPostSavedState(postName: String, isSaved: Boolean): Boolean {
        return try {
            if (isSaved) {
                redditService.redditApi.savePost(postName)
            } else {
                redditService.redditApi.unsavePost(postName)
            }
            true
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            false
        } catch (e: HttpException) {
            handleHttpException(e, ::setPostSavedState.name)
            false
        } catch (e: Exception) {
            logError(::setPostSavedState.name, e)
            false
        }
    }

    private fun emptyCommentListingData(): CommentListingData =
        CommentListingData(emptyList())

}