package com.example.finalattestationreddit.domain.subreddits

import androidx.paging.PagingData
import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchSubredditsUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    operator fun invoke(query: String): Flow<PagingData<SubredditData>> =
        redditRepository.searchSubreddits(query)

}