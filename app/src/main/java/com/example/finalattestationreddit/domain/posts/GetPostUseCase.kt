package com.example.finalattestationreddit.domain.posts

import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.data.model.dto.post.Post
import javax.inject.Inject

class GetPostUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    suspend operator fun invoke(postName: String): Post? =
        redditRepository.getFirstPost(postName)

}