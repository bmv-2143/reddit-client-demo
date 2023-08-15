package com.example.finalattestationreddit.domain.posts

import com.example.finalattestationreddit.data.repositories.RedditRepository
import javax.inject.Inject

class ClearSavedPostUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    suspend operator fun invoke(): Boolean =
        redditRepository.clearSavedPosts()

}