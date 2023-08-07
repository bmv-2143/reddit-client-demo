package com.example.finalattestationreddit.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.CompoundViewGroupScoreVotingBinding
import com.example.finalattestationreddit.presentation.utils.formatLargeNumber


class ScoreVotingViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding =
        CompoundViewGroupScoreVotingBinding.inflate(
            LayoutInflater.from(context), this, true)

    var onUpVoteClickListener: (() -> Unit)? = null
    var onDownVoteClickListener: (() -> Unit)? = null

    private var currentVoteState = VoteState.INITIAL

    init {
        binding.compoundViewGroupScoreUpvote.setOnClickListener {
            upVote()
            onUpVoteClickListener?.invoke()
        }

        binding.compoundViewGroupScoreDownvote.setOnClickListener {
            downVote()
            onDownVoteClickListener?.invoke()
        }
    }

    private fun downVote() {
        if (currentVoteState == VoteState.DOWN_VOTED) {
            setVoteState(VoteState.INITIAL)
        } else {
            setVoteState(VoteState.DOWN_VOTED)
        }
    }

    private fun upVote() {
        if (currentVoteState == VoteState.UP_VOTED) {
            setVoteState(VoteState.INITIAL)
        } else {
            setVoteState(VoteState.UP_VOTED)
        }
    }

    fun setScore(score: Int) {
        binding.compoundViewGroupScoreText.text = formatLargeNumber(score)
    }

    fun setVoteState(voteState: VoteState) {
        currentVoteState = voteState
        voteState.activate(binding)

    }

    enum class VoteState {
        INITIAL {
            override fun activate(binding: CompoundViewGroupScoreVotingBinding) {
                binding.compoundViewGroupScoreUpvote
                    .setImageResource(R.drawable.arrow_up_boxed_empty)
                binding.compoundViewGroupScoreDownvote
                    .setImageResource(R.drawable.arrow_down_boxed_empty)
            }
        },
        UP_VOTED {
            override fun activate(binding: CompoundViewGroupScoreVotingBinding) {
                binding.compoundViewGroupScoreUpvote
                    .setImageResource(R.drawable.arrow_up_boxed_filled)
                binding.compoundViewGroupScoreDownvote
                    .setImageResource(R.drawable.arrow_down_boxed_empty)
            }
        },
        DOWN_VOTED {
            override fun activate(binding: CompoundViewGroupScoreVotingBinding) {
                binding.compoundViewGroupScoreUpvote
                    .setImageResource(R.drawable.arrow_up_boxed_empty)
                binding.compoundViewGroupScoreDownvote
                    .setImageResource(R.drawable.arrow_down_boxed_filled)
            }
        };

        abstract fun activate(binding: CompoundViewGroupScoreVotingBinding)
    }
}
