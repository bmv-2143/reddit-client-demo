package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.domain.GetSubredditUseCase
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditInfoViewModel @Inject constructor(
    private val getSubredditUseCase: GetSubredditUseCase
) : ViewModel() {

    internal fun getSubredditUrl(subredditData : SubredditData) : String {
        return "${redditBaseUrl}${subredditData.url}"
    }

    private val _subredditFlow = MutableSharedFlow<SubredditData>()
    internal val subredditFlow = _subredditFlow.asSharedFlow()

    internal fun loadSubreddit(subredditDisplayName : String) {
        viewModelScope.launch {
            val subreddit = getSubredditUseCase(subredditDisplayName)

            if (subreddit != null) {
                _subredditFlow.emit(subreddit)
            } else {
                Log.e(TAG, "${::loadSubreddit}: subreddit is null")
            }
        }
    }

    companion object {
        private const val redditBaseUrl = "https://www.reddit.com"
    }

}