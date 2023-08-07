package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import com.example.finalattestationreddit.data.dto.post.Post

fun interface PostItemClickListener {
    fun onPostItemClick(post: Post)
}