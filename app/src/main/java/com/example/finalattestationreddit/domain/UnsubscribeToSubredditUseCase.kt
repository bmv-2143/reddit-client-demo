package com.example.finalattestationreddit.domain

import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.data.SubscriptionUpdateResult
import com.example.finalattestationreddit.data.network.SubredditSubscription
import javax.inject.Inject

class UnsubscribeFromSubredditUseCase @Inject constructor(
    private val redditRepository: RedditRepository,
) {

    suspend operator fun invoke(subredditName : String): SubscriptionUpdateResult =
        redditRepository.updateSubscription(subredditName, SubredditSubscription.ACTION_UNSUBSCRIBE)

}