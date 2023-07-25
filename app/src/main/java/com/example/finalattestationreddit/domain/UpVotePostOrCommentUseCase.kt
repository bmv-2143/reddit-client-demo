package com.example.finalattestationreddit.domain

import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.VoteDirections
import javax.inject.Inject

class UpVotePostOrCommentUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(targetId: String) =
        redditRepository.vote(targetId, VoteDirections.UP_VOTE)

}