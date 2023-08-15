package com.example.finalattestationreddit.domain.comments

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class GetPostCommentsUseCase @Inject constructor(
    private val redditRepository: RedditRepository
) {

    suspend operator fun invoke(
        subredditDisplayName: String,
        postName: String,
        commentsCountLimit: Int
    ) = redditRepository.getPostComments(subredditDisplayName, postName, commentsCountLimit)

}