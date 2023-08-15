package com.example.finalattestationreddit.domain.posts_comments

import com.example.finalattestationreddit.data.repositories.RedditRepository
import javax.inject.Inject

class UpVotePostOrCommentUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(targetId: String) =
        redditRepository.vote(targetId, VoteDirections.UP_VOTE)

}