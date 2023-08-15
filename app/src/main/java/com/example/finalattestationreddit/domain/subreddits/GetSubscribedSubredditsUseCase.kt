package com.example.finalattestationreddit.domain.subreddits

import androidx.paging.PagingData
import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSubscribedSubredditsUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    operator fun invoke(): Flow<PagingData<SubredditData>> =
        redditRepository.getSubscribedSubreddits()

}