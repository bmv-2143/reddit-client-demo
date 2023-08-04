package com.example.finalattestationreddit.presentation.bottom_navigation.post

import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.databinding.ListItemPostCommentBinding

class PostCommentsAdapterViewHolder(
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

    private fun setTexts(comment: Comment) {
        binding.listItemPostCommentAuthor.text = comment.author
        binding.listItemPostCommentTextBody.text = comment.body

        comment.score?.let {
            binding.fragmentPostInfoScoreVoting.setScore(it)
        }

        comment.createdUtc?.let { createdUtc ->
            binding.listItemPostCommentPublicationTime.text =
                formatElapsedTimeAction(createdUtc)
        }
    }

    private fun setDownloadButtonClickListener(comment: Comment) {
        binding.listItemPostCommentButtonLocalDownload.setOnClickListener {
            onDownloadButtonClick(comment)
        }
    }

    private fun setSaveButtonClickListener(comment: Comment) {
        binding.listItemPostCommentButtonSaveButton.setOnClickListener {
            onSaveButtonClick(comment)
        }
    }

    private fun setVoteButtonsClickListeners(comment: Comment) {
        binding.fragmentPostInfoScoreVoting.onUpVoteClickListener = {
            onCommentUpVoteClick(comment)
        }
        binding.fragmentPostInfoScoreVoting.onDownVoteClickListener = {
            onCommentDownVoteClick(comment)
        }
    }
}