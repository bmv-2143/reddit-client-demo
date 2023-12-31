package com.example.finalattestationreddit.domain.friends

import com.example.finalattestationreddit.data.repositories.RedditRepository
import javax.inject.Inject

class GetFriendsUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    internal suspend operator fun invoke() = redditRepository.getFriends()

}