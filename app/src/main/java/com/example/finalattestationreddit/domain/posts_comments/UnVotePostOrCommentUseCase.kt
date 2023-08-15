package com.example.finalattestationreddit.domain.posts_comments

import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.data.VoteDirections
import javax.inject.Inject

class UnVotePostOrCommentUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(targetId: String) =
        redditRepository.vote(targetId, VoteDirections.UN_VOTE)

}