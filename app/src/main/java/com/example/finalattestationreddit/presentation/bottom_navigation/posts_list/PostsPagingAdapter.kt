package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.finalattestationreddit.databinding.ListItemPostBinding
import com.example.finalattestationreddit.data.dto.post.Post

class PostsPagingAdapter(private val onItemClicked: (Post) -> Unit) :
    PagingDataAdapter<Post, PostsAdapterViewHolder>(PostsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapterViewHolder {
        val binding = ListItemPostBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsAdapterViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: PostsAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

}