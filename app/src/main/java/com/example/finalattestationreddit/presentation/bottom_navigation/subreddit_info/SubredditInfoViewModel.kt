package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.BuildConfig
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.domain.subreddits.GetSubredditUseCase
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubredditInfoViewModel @Inject constructor(
    private val getSubredditUseCase: GetSubredditUseCase
) : ViewModel() {

    internal fun getSubredditUrl(subredditData : SubredditData) : String {
        return "${BuildConfig.REDDIT_BASE_URL}${subredditData.url}"
    }

    private val _subredditFlow = MutableStateFlow<SubredditData?>(null)
    internal val subredditFlow = _subredditFlow.asStateFlow()

    internal fun setSubreddit(subredditData : SubredditData) {
        _subredditFlow.value = subredditData
    }

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

}