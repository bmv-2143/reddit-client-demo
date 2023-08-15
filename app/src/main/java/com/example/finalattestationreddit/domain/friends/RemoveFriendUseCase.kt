package com.example.finalattestationreddit.domain.friends

import com.example.finalattestationreddit.data.repositories.RedditRepository
import javax.inject.Inject

class RemoveFriendUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(userName: String): Boolean =
        redditRepository.removeFriend(userName)

}