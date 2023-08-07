package com.example.finalattestationreddit.data

import android.util.Log
import com.example.finalattestationreddit.data.dto.comment.CommentData
import com.example.finalattestationreddit.data.dto.comment.CommentListingData
import com.example.finalattestationreddit.data.dto.post.PostData
import com.example.finalattestationreddit.data.dto.post.PostListingData
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.dto.subreddit.SubredditListingData
import com.example.finalattestationreddit.data.dto.user.Friend
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.data.mappers.toSubredditDataList
import com.example.finalattestationreddit.log.TAG
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

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
            handleHttpException(e)
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
                NetworkError.NoInternetConnection(
                    e.message ?: "No internet connection"
                )
            )
        )
    }

    private suspend fun handleHttpException(e: HttpException) {
        Log.e(
            TAG,
            "Error in ${getParentFunctionName()} : ${::handleHttpException.name} error: ${e.message}"
        )
        when (e.code()) {
            403 -> _networkErrorFlow.emit(Event(NetworkError.ForbiddenApiRateExceeded(e.message())))

            401 -> _networkErrorFlow.emit(Event(NetworkError.Unauthorized(e.message())))

            else -> _networkErrorFlow.emit(Event(NetworkError.HttpError(e.message())))
        }
    }

    private fun logError(methodName: String, e: Exception) {
        Log.e(TAG, "$methodName error: ${e.message}")
    }

    private fun emptySubredditListingData(): SubredditListingData =
        SubredditListingData(emptyList(), null, null)

    suspend fun updateSubscription(subredditName: String, action: String): Boolean {
        return try {
            redditService.redditApi.updateSubscription(subredditName, action)
            true
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            false
        } catch (e: HttpException) {
            handleHttpException(e)
            false
        } catch (e: Exception) {
            logError(::updateSubscription.name, e)
            false
        }
    }

    suspend fun getSubscribedSubreddits(): List<SubredditData> {
        return try {
            redditService.redditApi.getSubscribedSubreddits().toSubredditDataList()
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyList()
        } catch (e: HttpException) {
            handleHttpException(e)
            emptyList()
        } catch (e: Exception) {
            logError(::getSubscribedSubreddits.name, e)
            emptyList()
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
            handleHttpException(e)
            emptyPostListingData()
        } catch (e: Exception) {
            logError(::getSubredditPosts.name, e)
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
            handleHttpException(e)
            null
        } catch (e: Exception) {
            logError(::getSubreddits.name, e)
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
            handleHttpException(e)
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
        depth: Int? = null
    ): CommentListingData {
        return try {
            Log.e(TAG, "getPostComments START")
            getOnlyCommentsToPost(
                subredditDisplayName,
                postName,
                depth = 1,
                limit = limit
            ) // todo: hardcoded depth
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyCommentListingData()
        } catch (e: HttpException) {
            handleHttpException(e)
            emptyCommentListingData()
        } catch (e: Exception) {
            logError(::getPostComments.name, e)
            emptyCommentListingData()
        }
    }

    private suspend fun getOnlyCommentsToPost(
        subredditDisplayName: String,
        postName: String,
        depth: Int?,
        limit: Int?
    ): CommentListingData {
        val response =
            redditService.redditApi.getPostComments(
                subredditDisplayName,
                postName,
                depth = depth,
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
            handleHttpException(e)
            null
        } catch (e: Exception) {
            logError(::getUser.name, e)
            null
        }
    }

    internal suspend fun getUserPosts(username: String, after: String, perPage: Int): PostListingData {
        return try {
            redditService.redditApi.getUserPosts(username, after, perPage).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyPostListingData()
        } catch (e: HttpException) {
            handleHttpException(e)
            emptyPostListingData()
        } catch (e: Exception) {
            logError(::getUserPosts.name, e)
            emptyPostListingData()
        }
    }

    internal suspend fun getUserPostsAll(username: String): List<PostData>  {
        return try {
            redditService.redditApi.getUserPostsAll(username).data.children
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyList()
        } catch (e: HttpException) {
            handleHttpException(e)
            emptyList()
        } catch (e: Exception) {
            logError(::getUserPostsAll.name, e)
            emptyList()
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
            handleHttpException(e)
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
            handleHttpException(e)
            false
        } catch (e: Exception) {
            logError(::addFriend.name, e)
            false
        }
    }

    internal suspend fun getFriends() : List<Friend>  {
        return try {
            redditService.redditApi.getFriends().data.children
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyList()
        } catch (e: HttpException) {
            handleHttpException(e)
            emptyList()
        } catch (e: Exception) {
            logError(::getFriends.name, e)
            emptyList()
        }
    }

    private fun emptyCommentListingData(): CommentListingData =
        CommentListingData(emptyList())

    private fun getParentFunctionName(): String {
        val stackTrace = Thread.currentThread().stackTrace
        return stackTrace[2].methodName
    }

}