package com.example.finalattestationreddit.data.network

import com.example.finalattestationreddit.data.dto.ListingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditApi {

    // TODO: it might be better to parameterize new / popular in path with {type} and pass it as a parameter
    @GET("subreddits/new")
    suspend fun getNewSubreddits(
        @Query("after") after: String,
        @Query("limit") perPage: Int
    ): ListingResponse

    @GET("subreddits/popular")
    suspend fun getPopularSubreddits(): ListingResponse

}