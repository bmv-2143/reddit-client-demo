package com.example.finalattestationreddit.data.network

import com.example.finalattestationreddit.data.dto.ListingResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RedditApi {

    @GET("subreddits/{type}")
    suspend fun getSubreddits(
        @Path("type") subredditsListType: String,
        @Query("after") after: String,
        @Query("limit") perPage: Int
    ): ListingResponse

    @GET("subreddits/new")
    suspend fun getNewSubreddits(
        @Query("after") after: String,
        @Query("limit") perPage: Int
    ): ListingResponse

    @GET("subreddits/popular")
    suspend fun getPopularSubreddits(): ListingResponse


    @POST("/api/subscribe")
    suspend fun updateSubscription(
        @Query("sr_name") subredditName: String,
        @Query("action") action: String
    )

    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribedSubreddits(): ListingResponse

}