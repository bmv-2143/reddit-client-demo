package com.example.finalattestationreddit.domain

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class ClearSavedPostUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    suspend operator fun invoke(): Boolean =
        redditRepository.clearSavedPosts()

}