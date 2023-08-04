package com.example.finalattestationreddit.presentation.bottom_navigation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.databinding.ListItemPostCommentBinding

class PostCommentsAdapter constructor(
    private val formatElapsedTimeAction: (Long) -> String,
    private val onDownloadButtonClick: (Comment) -> Unit,
    private val onSaveButtonClick: (Comment) -> Unit,
    private val onCommentUpVoteClick: (Comment) -> Unit,
    private val onCommentDownVoteClick: (Comment) -> Unit,
) : ListAdapter<Comment, PostCommentsAdapterViewHolder>(PostCommentsAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentsAdapterViewHolder {
        val binding = ListItemPostCommentBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PostCommentsAdapterViewHolder(
            binding,
            formatElapsedTimeAction,
            onDownloadButtonClick,
            onSaveButtonClick,
            onCommentUpVoteClick,
            onCommentDownVoteClick
        )
    }

    override fun onBindViewHolder(holder: PostCommentsAdapterViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

}

class PostCommentsAdapterDiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}
