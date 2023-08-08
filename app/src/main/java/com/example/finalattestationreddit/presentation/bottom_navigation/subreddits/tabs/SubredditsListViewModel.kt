package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.network.SubredditListType.ARG_SUBREDDITS_LIST_TYPE
import com.example.finalattestationreddit.data.network.SubredditListType.VAL_SUBREDDITS_LIST_TYPE_SUBSCRIBED
import com.example.finalattestationreddit.domain.GetSubredditsUseCase
import com.example.finalattestationreddit.domain.GetSubscribedSubredditsUseCase
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SubredditsListViewModel @Inject constructor(
    private val getSubredditsUseCase: GetSubredditsUseCase,
    private val getSubscribedSubredditsUseCase: GetSubscribedSubredditsUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    internal val subredditsPagedFlow: Flow<PagingData<SubredditData>>?
        get() {
            val subredditsListType: String? = savedStateHandle[ARG_SUBREDDITS_LIST_TYPE]
            return makeSubredditsFlow(subredditsListType)
        }

    private fun makeSubredditsFlow(subredditsListType: String?) =
        when (subredditsListType) {
            null -> {
                Log.e(TAG, "subredditsListType is null")
                null
            }

            VAL_SUBREDDITS_LIST_TYPE_SUBSCRIBED ->
                getSubscribedSubredditsUseCase().cachedIn(viewModelScope)

            else -> getSubredditsUseCase(subredditsListType).cachedIn(viewModelScope)

        }
}