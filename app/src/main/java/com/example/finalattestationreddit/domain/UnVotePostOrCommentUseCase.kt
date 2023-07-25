package com.example.finalattestationreddit.domain

import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.VoteDirections
import javax.inject.Inject

class UnVotePostOrCommentUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(targetId: String) =
        redditRepository.vote(targetId, VoteDirections.UN_VOTE)

}