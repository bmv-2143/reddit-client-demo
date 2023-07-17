package com.example.finalattestationreddit.domain

import androidx.paging.PagingData
import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.dto.SubredditData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubredditsUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    operator fun invoke(): Flow<PagingData<SubredditData>> =
        redditRepository.getNewSubreddits()

}