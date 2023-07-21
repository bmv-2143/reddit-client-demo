package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import androidx.lifecycle.ViewModel
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubredditInfoViewModel @Inject constructor() : ViewModel() {

    internal fun getSubredditUrl(subredditData : SubredditData) : String {
        return "${redditBaseUrl}${subredditData.url}"
    }

    companion object {
        private const val redditBaseUrl = "https://www.reddit.com"
    }

}