package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData
import com.example.finalattestationreddit.databinding.ListItemSubredditBinding

class SubredditsAdapterViewHolder(
    private val binding: ListItemSubredditBinding,
    private val onClick: (SubredditData) -> Unit,
    private val onItemSubscribeButtonClick: (SubredditData) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: SubredditData? = null

    init {
        setSubredditListItemClickListener()
        setSubredditSubscribeButtonClickListener()
    }

    private fun setSubredditSubscribeButtonClickListener() {
        binding.listItemSubredditButtonUserSubscribed.setOnClickListener {
            currentItem?.let {
                onItemSubscribeButtonClick(it)
            }
        }
    }

    private fun setSubredditListItemClickListener() {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(subredditItem: SubredditData) {
        currentItem = subredditItem
        setDisplayNamePrefixed(subredditItem)
        setPublicDescription(subredditItem)
        updateSubscriptionStatusImage(subredditItem)
    }

    private fun updateSubscriptionStatusImage(subredditItem: SubredditData) {
        binding.listItemSubredditButtonUserSubscribed.setImageDrawable(
            subredditItem.userIsSubscriber?.let { userIsSubscriber ->
                if (userIsSubscriber) {
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.ic_list_item_subreddit_user_subscribed
                    )
                } else {
                    AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.ic_list_item_subreddit_user_not_subscribed
                    )
                }
            }
        )
    }

    private fun setDisplayNamePrefixed(subredditItem: SubredditData) {
        binding.listItemSubredditDisplayName.text =
            subredditItem.displayNamePrefixed
    }

    private fun setPublicDescription(subredditItem: SubredditData) {
        if (subredditItem.publicDescription.isNullOrEmpty()) {
            binding.listItemSubredditDescription.visibility = View.GONE
        } else {
            binding.listItemSubredditDescription.visibility = View.VISIBLE
            binding.listItemSubredditDescription.text = subredditItem.publicDescription
        }
    }
}