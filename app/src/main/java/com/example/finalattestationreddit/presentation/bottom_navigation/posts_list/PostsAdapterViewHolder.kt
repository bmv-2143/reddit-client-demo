package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.unsplashattestationproject.databinding.ListItemPostBinding

class PostsAdapterViewHolder(
    private val binding: ListItemPostBinding,
    private val onClick: (Post) -> Unit,
    private val onItemSubscribeButtonClick: (Post) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: Post? = null

    init {
        setPostsListItemClickListener()
//        setSubredditSubscribeButtonClickListener()
    }

//    private fun setSubredditSubscribeButtonClickListener() {
//        binding.listItemSubredditButtonUserSubscribed.setOnClickListener {
//            currentItem?.let {
//                onItemSubscribeButtonClick(it)
//            }
//        }
//    }

    private fun setPostsListItemClickListener() {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(postItem: Post) {
        currentItem = postItem
        loadTexts(postItem)
//        loadAvatar(subredditItem)
//        loadPhoto(subredditItem.coverPhoto?.urls?.regular ?: "")

//        updateSubscriptionStatusImage(postItem)
    }

//    private fun updateSubscriptionStatusImage(postItem: Post) {
//        binding.listItemSubredditButtonUserSubscribed.setImageDrawable(
//            if (subredditItem.userIsSubscriber) {
//                AppCompatResources.getDrawable(
//                    binding.root.context,
//                    R.drawable.ic_list_item_subreddit_user_subscribed
//                )
//            } else {
//                AppCompatResources.getDrawable(
//                    binding.root.context,
//                    R.drawable.ic_list_item_subreddit_user_not_subscribed
//                )
//            }
//        )
//    }

    private fun loadTexts(postItem: Post) {

//        binding..text = subredditItem.id
        binding.listItemPostDisplayName.text =
            postItem.title // TODO: fix me

//        if (postItem.publicDescription.isEmpty()) {
//            binding.listItemSubredditDescription.visibility = View.GONE
//        } else {
//            binding.listItemSubredditDescription.visibility = View.VISIBLE
//            binding.listItemSubredditDescription.text = subredditItem.publicDescription
//        }
//
//        binding.listItemSubredditDescription.text = subredditItem.publicDescription

    }


}