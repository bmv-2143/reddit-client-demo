package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.unsplashattestationproject.databinding.ListItemPostBinding
import com.example.unsplashattestationproject.databinding.ListItemSubredditBinding

class PostsPagingAdapter(
    private val onItemClicked: (Post) -> Unit,
    private val onItemSubscribeButtonClick: (Post) -> Unit
) : PagingDataAdapter<Post, PostsAdapterViewHolder>(PostsDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsAdapterViewHolder {
        val binding = ListItemPostBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PostsAdapterViewHolder(binding, onItemClicked, onItemSubscribeButtonClick)
    }

    override fun onBindViewHolder(holder: PostsAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

//    suspend fun updateItem(subredditData: SubredditData) {
//        val currentList = snapshot().items.toMutableList()
//        val index = currentList.indexOfFirst { it.id == subredditData.id }
//        if (index != -1) {
//            currentList[index] = subredditData
//            submitData(PagingData.from(currentList))
//            notifyItemChanged(index)
//        }
//    }
}