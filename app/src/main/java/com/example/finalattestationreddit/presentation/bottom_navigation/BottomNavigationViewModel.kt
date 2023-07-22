package com.example.finalattestationreddit.presentation.bottom_navigation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.SubscriptionUpdateResult
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.domain.SubscribeToSubredditUseCase
import com.example.finalattestationreddit.domain.UnsubscribeFromSubredditUseCase
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavigationViewModel @Inject constructor(
    application: Application,
    private val subscribeToSubredditUseCase: SubscribeToSubredditUseCase,
    private val unsubscribeFromSubredditUseCase: UnsubscribeFromSubredditUseCase
) : AndroidViewModel(application) {

    private val _updatedSubscriptionFlow = MutableSharedFlow<SubredditData>()
    val subscriptionUpdatesFlow = _updatedSubscriptionFlow.asSharedFlow()

    fun switchSubscription(subredditData: SubredditData) {
        viewModelScope.launch {
            val updatedResult: SubscriptionUpdateResult = invertSubscription(subredditData)

            if (updatedResult.subscriptionUpdateSuccess) {
                _updatedSubscriptionFlow.emit(
                    subredditData.copy(userIsSubscriber = !subredditData.userIsSubscriber))
            } else {
                Log.e(TAG, "Failed to update subscription status")
            }
        }
    }

    private suspend fun invertSubscription(
        subredditData: SubredditData
    ) = if (subredditData.userIsSubscriber) {
        unsubscribeFromSubredditUseCase(subredditData.displayName)
    } else {
        subscribeToSubredditUseCase(subredditData.displayName)
    }

}