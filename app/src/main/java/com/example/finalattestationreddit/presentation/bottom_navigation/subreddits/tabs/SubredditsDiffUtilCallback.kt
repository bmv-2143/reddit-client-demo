package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import androidx.recyclerview.widget.DiffUtil
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData

class SubredditsDiffUtilCallback : DiffUtil.ItemCallback<SubredditData>() {

    override fun areItemsTheSame(
        oldItem: SubredditData,
        newItem: SubredditData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SubredditData,
        newItem: SubredditData
    ): Boolean {
        return oldItem == newItem
    }
}