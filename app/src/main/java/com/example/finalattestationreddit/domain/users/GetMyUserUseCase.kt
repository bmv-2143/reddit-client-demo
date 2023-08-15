package com.example.finalattestationreddit.domain.users

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class GetMyUserUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke() =
        redditRepository.getMyUser()

}