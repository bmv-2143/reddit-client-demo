package com.example.finalattestationreddit.data.network

import com.example.finalattestationreddit.data.dto.ListingResponse
import retrofit2.http.GET

interface RedditApi {

    @GET("subreddits/new")
    suspend fun getNewSubreddits(): ListingResponse

    @GET("subreddits/popular")
    suspend fun getPopularSubreddits(): ListingResponse

}