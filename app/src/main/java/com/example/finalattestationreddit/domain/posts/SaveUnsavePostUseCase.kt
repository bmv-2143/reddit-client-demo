package com.example.finalattestationreddit.domain.posts

import com.example.finalattestationreddit.data.repositories.RedditRepository
import javax.inject.Inject

class SaveUnsavePostUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    suspend operator fun invoke(postName: String, isSaved : Boolean): Boolean =
        redditRepository.setSavePostState(postName, isSaved)

}