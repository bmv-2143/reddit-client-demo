package com.example.finalattestationreddit.domain.posts

import androidx.paging.PagingData
import androidx.paging.map
import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.data.model.dto.post.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllRecentPostsUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    operator fun invoke(): Flow<PagingData<Post>> =
        redditRepository.getAllRecentPosts()
            .map { pagingData ->
                pagingData.map { post ->
                    post.data
                }
            }

}