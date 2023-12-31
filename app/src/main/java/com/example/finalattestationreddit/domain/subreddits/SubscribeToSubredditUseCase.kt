package com.example.finalattestationreddit.domain.subreddits

import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.data.model.subscription.SubscriptionUpdateResult
import javax.inject.Inject

class SubscribeToSubredditUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    suspend operator fun invoke(subredditName : String): SubscriptionUpdateResult =
        redditRepository.updateSubscription(subredditName, SubredditSubscription.ACTION_SUBSCRIBE)

}