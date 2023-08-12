package com.example.finalattestationreddit.domain

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    internal suspend operator fun invoke() = redditRepository.getFriends()

}