package com.example.finalattestationreddit.presentation.bottom_navigation.post

import android.view.LayoutInflater
import android.view.ViewGroup
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

    fun updateComment(updatedComment: Comment) {
        val position = currentList.indexOfFirst { it.name == updatedComment.name }
        if (position != -1) {
            val newList = currentList.toMutableList()
            newList[position] = updatedComment
            submitList(newList)
        }
    }


}