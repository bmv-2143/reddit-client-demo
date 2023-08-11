package com.example.finalattestationreddit.domain

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val redditRepository: RedditRepository) {

    operator fun invoke() = redditRepository.removeAccessTokenSync()

}