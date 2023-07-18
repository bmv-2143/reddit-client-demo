package com.example.finalattestationreddit.data

import android.util.Log
import com.example.finalattestationreddit.data.dto.subreddit.SubredditListingData
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.mappers.toSubredditDataList
import com.example.finalattestationreddit.log.TAG
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class RedditNetworkDataSource @Inject constructor(private val redditService: RedditService) {

    private val _networkErrorFlow = MutableSharedFlow<NetworkError>()
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
            emptyListingData()
        } catch (e: HttpException) {
            handleHttpException(e)
            emptyListingData()
        } catch (e: Exception) {
            logError(::getSubreddits.name, e)
            emptyListingData()
        }
    }

    private suspend fun handleUnknownHostError(e: UnknownHostException) {
        logError(::handleUnknownHostError.name, e)
        _networkErrorFlow.emit(
            NetworkError.NoInternetConnection(
                e.message ?: "No internet connection"
            )
        )
    }

    private suspend fun handleHttpException(e: HttpException) {
        Log.e(TAG, "${::handleHttpException.name} error: ${e.message}")
        when (e.code()) {
            403 -> _networkErrorFlow.emit(NetworkError.ForbiddenApiRateExceeded(e.message()))

            401 -> _networkErrorFlow.emit(NetworkError.Unauthorized(e.message()))

            else -> _networkErrorFlow.emit(NetworkError.HttpError(e.message()))
        }
    }

    private fun logError(methodName: String, e: Exception) {
        Log.e(TAG, "$methodName error: ${e.message}")
    }

    private fun emptyListingData(): SubredditListingData = SubredditListingData(emptyList(), null, null)

    suspend fun updateSubscription(subredditName: String, action: String) : Boolean {
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
            logError(::getSubreddits.name, e)
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
            logError(::getSubreddits.name, e)
            emptyList()
        }
    }

    suspend fun getSubredditPosts(
        subredditDisplayName: String,
        after: String,
        perPage: Int
    ): SubredditListingData {

        return try {
            redditService.redditApi.getSubredditPosts(subredditDisplayName, after, perPage).data
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyListingData()
        } catch (e: HttpException) {
            handleHttpException(e)
            emptyListingData()
        } catch (e: Exception) {
            logError(::getSubreddits.name, e)
            emptyListingData()
        }
    }

}

