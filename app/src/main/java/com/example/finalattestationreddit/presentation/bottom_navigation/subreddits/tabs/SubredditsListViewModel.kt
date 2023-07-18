package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.SubscriptionUpdateResult
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.network.SubredditListType.ARG_SUBREDDITS_LIST_TYPE
import com.example.finalattestationreddit.domain.GetSubredditsUseCase
import com.example.finalattestationreddit.domain.SubscribeToSubredditUseCase
import com.example.finalattestationreddit.domain.UnsubscribeFromSubredditUseCase
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditsListViewModel @Inject constructor(
    private val getSubredditsUseCase: GetSubredditsUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val subscribeToSubredditUseCase: SubscribeToSubredditUseCase,
    private val unsubscribeFromSubredditUseCase: UnsubscribeFromSubredditUseCase
) : ViewModel() {

    internal val subredditsPagedFlow: Flow<PagingData<SubredditData>>?
        get() {
            val subredditsListType: String? =
                savedStateHandle[ARG_SUBREDDITS_LIST_TYPE]

            return if (subredditsListType != null) {
                getSubredditsUseCase(subredditsListType).cachedIn(viewModelScope)
            } else {
                Log.e(TAG, "subredditsListType is null")
                null
            }
        }

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