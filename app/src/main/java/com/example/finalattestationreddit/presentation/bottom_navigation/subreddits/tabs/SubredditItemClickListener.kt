package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData

fun interface SubredditItemClickListener {
    fun onSubredditItemClick(subreddit: SubredditData)
}