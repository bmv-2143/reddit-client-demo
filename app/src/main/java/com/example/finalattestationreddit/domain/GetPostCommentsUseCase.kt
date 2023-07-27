package com.example.finalattestationreddit.domain

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class GetPostCommentsUseCase  @Inject constructor(
    private val redditRepository: RedditRepository
) {

    suspend operator fun invoke(subredditDisplayName: String, postName: String) =
        redditRepository.getPostComments(subredditDisplayName, postName)

}