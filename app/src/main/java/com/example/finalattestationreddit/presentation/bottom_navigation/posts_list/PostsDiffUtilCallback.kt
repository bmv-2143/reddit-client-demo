package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import androidx.recyclerview.widget.DiffUtil
import com.example.finalattestationreddit.data.dto.post.Post

class PostsDiffUtilCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return oldItem.url == newItem.url // todo: check if it's correct, its better to compar by id
    }

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post
    ): Boolean {
        return oldItem == newItem
    }
}