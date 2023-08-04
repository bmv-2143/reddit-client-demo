package com.example.finalattestationreddit.presentation.bottom_navigation.post

import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.databinding.ListItemPostCommentBinding
import com.example.finalattestationreddit.presentation.widgets.ScoreVotingViewGroup

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
        updatePostVoteControls(comment)
    }

    private fun setTexts(comment: Comment) {
        binding.listItemPostCommentAuthor.text = comment.author
        binding.listItemPostCommentTextBody.text = comment.body

        comment.createdUtc?.let { createdUtc ->
            binding.listItemPostCommentPublicationTime.text = formatElapsedTimeAction(createdUtc)
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

    private fun updatePostVoteControls(comment: Comment) {
        comment.score?.let {
            binding.fragmentPostInfoScoreVoting.setScore(it)
        }

        when (comment.likedByUser) {
            true -> binding.fragmentPostInfoScoreVoting.setVoteState(
                ScoreVotingViewGroup.VoteState.UP_VOTED)

            false -> binding.fragmentPostInfoScoreVoting.setVoteState(
                ScoreVotingViewGroup.VoteState.DOWN_VOTED)

            null -> binding.fragmentPostInfoScoreVoting.setVoteState(
                ScoreVotingViewGroup.VoteState.INITIAL)
        }
    }
}