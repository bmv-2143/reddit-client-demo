package com.example.finalattestationreddit.data

import com.example.finalattestationreddit.data.network.RedditApi
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class RedditService @Inject constructor(@Named("api") redditService: Retrofit) {

    val redditApi: RedditApi =
        redditService.create(RedditApi::class.java)
}