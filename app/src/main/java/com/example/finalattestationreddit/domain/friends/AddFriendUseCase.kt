package com.example.finalattestationreddit.domain.friends

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class AddFriendUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(userName: String): Boolean =
        redditRepository.addFriend(userName)

}