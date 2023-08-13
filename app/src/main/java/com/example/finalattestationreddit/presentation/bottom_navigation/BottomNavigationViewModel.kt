package com.example.finalattestationreddit.presentation.bottom_navigation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.SubscriptionUpdateResult
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.domain.LogoutUseCase
import com.example.finalattestationreddit.domain.SubscribeToSubredditUseCase
import com.example.finalattestationreddit.domain.UnsubscribeFromSubredditUseCase
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavigationViewModel @Inject constructor(
    application: Application,
    redditRepository: RedditRepository,
    private val subscribeToSubredditUseCase: SubscribeToSubredditUseCase,
    private val unsubscribeFromSubredditUseCase: UnsubscribeFromSubredditUseCase,
    private val logoutUseCase: LogoutUseCase
) : AndroidViewModel(application) {

    val networkErrorsFlow = redditRepository.networkErrorsFlow

    private val _selectedSubredditFlow = MutableStateFlow<SubredditData?>(null)
    val selectedSubredditFlow = _selectedSubredditFlow.asStateFlow()

    internal fun setSelectedSubreddit(subreddit : SubredditData) {
        _selectedSubredditFlow.value = subreddit
    }

    private val _selectedPostFlow = MutableStateFlow<Post?>(null)
    val selectedPostFlow = _selectedPostFlow.asStateFlow()

    internal fun setSelectedPost(post : Post) {
        _selectedPostFlow.value = post
    }

    private val _updatedSubscriptionFlow = MutableSharedFlow<SubredditData>()
    val subscriptionUpdatesFlow = _updatedSubscriptionFlow.asSharedFlow()


    private val _selectedUserFlow = MutableStateFlow<String?>(null)
    val selectedUserFlow = _selectedUserFlow.asStateFlow()

    internal fun setSelectedUser(username : String) {
        _selectedUserFlow.value = username
    }

    fun switchSubscription(subredditData: SubredditData) {
        viewModelScope.launch {
            val updatedResult: SubscriptionUpdateResult = invertSubscription(subredditData)

            if (updatedResult.subscriptionUpdateSuccess) {
                subredditData.userIsSubscriber?.let { userIsSubscriber ->
                    _updatedSubscriptionFlow.emit(
                        subredditData.copy(userIsSubscriber = !userIsSubscriber))
                }
            } else {
                Log.e(TAG, "Failed to update subscription status")
            }
        }
    }

    private suspend fun invertSubscription(
        subredditData: SubredditData
    ): SubscriptionUpdateResult {
        return when (subredditData.userIsSubscriber) {
            true -> unsubscribeFromSubredditUseCase(subredditData.displayName)

            false -> subscribeToSubredditUseCase(subredditData.displayName)

            null -> SubscriptionUpdateResult(subredditData.displayName, false)
        }
    }


    private val _logoutEvent = MutableSharedFlow<Unit>()
    val logoutEvents: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    internal fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _logoutEvent.emit(Unit)
        }
    }

}