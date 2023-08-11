package com.example.finalattestationreddit.domain

import com.example.finalattestationreddit.data.RedditRepository
import javax.inject.Inject

class GetMyUserCase @Inject constructor(private val redditRepository: RedditRepository) {

    suspend operator fun invoke() =
        redditRepository.getMyUser()

}