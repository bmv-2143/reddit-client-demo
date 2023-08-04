package com.example.finalattestationreddit.presentation.bottom_navigation.post

import androidx.recyclerview.widget.DiffUtil
import com.example.finalattestationreddit.data.dto.comment.Comment

class PostCommentsAdapterDiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}