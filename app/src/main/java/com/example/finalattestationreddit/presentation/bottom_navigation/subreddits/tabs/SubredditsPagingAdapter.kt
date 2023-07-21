package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import com.example.finalattestationreddit.databinding.ListItemSubredditBinding
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData

class SubredditsPagingAdapter(
    private val onItemClicked: (SubredditData) -> Unit,
    private val onItemSubscribeButtonClick: (SubredditData) -> Unit
) : PagingDataAdapter<SubredditData, SubredditsAdapterViewHolder>(SubredditsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditsAdapterViewHolder {
        val binding = ListItemSubredditBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SubredditsAdapterViewHolder(binding, onItemClicked, onItemSubscribeButtonClick)
    }

    override fun onBindViewHolder(holder: SubredditsAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    suspend fun updateItem(subredditData: SubredditData) {
        val currentList = snapshot().items.toMutableList()
        val index = currentList.indexOfFirst { it.id == subredditData.id }
        if (index != -1) {
            currentList[index] = subredditData
            submitData(PagingData.from(currentList))
            notifyItemChanged(index)
        }
    }
}