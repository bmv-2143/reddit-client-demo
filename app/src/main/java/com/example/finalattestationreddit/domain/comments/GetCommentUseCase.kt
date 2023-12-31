package com.example.finalattestationreddit.domain.comments

import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.data.model.dto.comment.Comment
import javax.inject.Inject

class GetCommentUseCase @Inject constructor(
    private val redditRepository: RedditRepository
) {

    suspend operator fun invoke(commentName: String): Comment? =
        redditRepository.getComment(commentName)

}