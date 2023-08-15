package com.example.finalattestationreddit.domain.users

import com.example.finalattestationreddit.data.repositories.RedditRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke(userName: String) =
        redditRepository.getUser(userName)

}