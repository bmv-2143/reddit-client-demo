package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.finalattestationreddit.data.dto.SubredditData
import com.example.unsplashattestationproject.databinding.ListItemSubredditBinding

class SubredditsPagingAdapter(
    private val onItemClicked: (SubredditData) -> Unit
) : PagingDataAdapter<SubredditData, SubredditsAdapterViewHolder>(SubredditsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditsAdapterViewHolder {
        val binding = ListItemSubredditBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SubredditsAdapterViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: SubredditsAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}