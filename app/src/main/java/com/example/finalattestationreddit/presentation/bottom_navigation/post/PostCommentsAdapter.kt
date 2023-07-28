package com.example.finalattestationreddit.presentation.bottom_navigation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.databinding.ListItemPostCommentBinding

class PostCommentsAdapter constructor(private val formatElapsedTimeAction: (Long) -> String) :
    ListAdapter<Comment, PostCommentsAdapter.ViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, formatElapsedTimeAction)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    class ViewHolder(
        private val binding: ListItemPostCommentBinding,
        private val formatElapsedTimeAction: (Long) -> String
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            setTexts(comment)
        }

        private fun setTexts(comment: Comment) {
            binding.listItemPostCommentAuthor.text = comment.author
            binding.listItemPostCommentTextBody.text = comment.body

            comment.createdUtc?.let { createdUtc ->
                binding.listItemPostCommentPublicationTime.text =
                    formatElapsedTimeAction(createdUtc)
            }
        }
    }
}

class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}
