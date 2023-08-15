package com.example.finalattestationreddit.domain.posts

import androidx.paging.PagingData
import androidx.paging.map
import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.data.dto.post.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetMySavedPostsUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    operator fun invoke(): Flow<PagingData<Post>> =
        redditRepository.getMySavedPosts()
            .map { pagingData ->
                pagingData.map { post ->
                    post.data
                }
            }

}