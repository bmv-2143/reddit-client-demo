package com.example.finalattestationreddit.presentation.bottom_navigation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.databinding.ListItemPostCommentBinding

class PostCommentsAdapter constructor(
    private val formatElapsedTimeAction: (Long) -> String,
    private val onDownloadButtonClick: (Comment) -> Unit,
    private val onSaveButtonClick: (Comment) -> Unit,
    private val onCommentUpVoteClick: (Comment) -> Unit,
    private val onCommentDownVoteClick: (Comment) -> Unit,
) : ListAdapter<Comment, PostCommentsAdapter.ViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemPostCommentBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding,
            formatElapsedTimeAction,
            onDownloadButtonClick,
            onSaveButtonClick,
            onCommentUpVoteClick,
            onCommentDownVoteClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    class ViewHolder(
        private val binding: ListItemPostCommentBinding,
        private val formatElapsedTimeAction: (Long) -> String,
        private val onDownloadButtonClick: (Comment) -> Unit,
        private val onSaveButtonClick: (Comment) -> Unit,
        private val onCommentUpVoteClick: (Comment) -> Unit,
        private val onCommentDownVoteClick: (Comment) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            setTexts(comment)
            setDownloadButtonClickListener(comment)
            setSaveButtonClickListener(comment)
            setVoteButtonsClickListeners(comment)
        }

        private fun setVoteButtonsClickListeners(comment: Comment) {
            binding.fragmentPostInfoScoreVoting.onUpVoteClickListener = {
                onCommentUpVoteClick(comment)
            }
            binding.fragmentPostInfoScoreVoting.onDownVoteClickListener = {
                onCommentDownVoteClick(comment)
            }
        }

        private fun setSaveButtonClickListener(comment: Comment) {
            binding.listItemPostCommentButtonSaveButton.setOnClickListener {
                onSaveButtonClick(comment)
            }
        }

        private fun setDownloadButtonClickListener(comment: Comment) {
            binding.listItemPostCommentButtonLocalDownload.setOnClickListener {
                onDownloadButtonClick(comment)
            }
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
