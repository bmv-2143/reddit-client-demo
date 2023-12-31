package com.example.finalattestationreddit.domain.friends

import com.example.finalattestationreddit.data.repositories.RedditRepository
import javax.inject.Inject

class CheckIfIsAFriendUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(userName: String) : Boolean =
        redditRepository.isFriend(userName)

}