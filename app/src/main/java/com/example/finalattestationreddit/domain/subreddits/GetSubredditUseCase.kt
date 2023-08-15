package com.example.finalattestationreddit.domain.subreddits

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class GetSubredditUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(subredditName: String) =
        redditRepository.getSubreddit(subredditName)

}