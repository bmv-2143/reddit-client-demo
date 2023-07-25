package com.example.finalattestationreddit.data.network

import android.support.annotation.IntRange
import com.example.finalattestationreddit.data.dto.post.PostListingResponse
import com.example.finalattestationreddit.data.dto.subreddit.SubredditListingResponse
import com.example.finalattestationreddit.data.dto.subreddit.SubredditResponse
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
    ): SubredditListingResponse

    @GET("subreddits/new")
    suspend fun getNewSubreddits(
        @Query("after") after: String,
        @Query("limit") perPage: Int
    ): SubredditListingResponse

    @GET("subreddits/popular")
    suspend fun getPopularSubreddits(): SubredditListingResponse


    @POST("/api/subscribe")
    suspend fun updateSubscription(
        @Query("sr_name") subredditName: String,
        @Query("action") action: String
    )

    @GET("/subreddits/mine/subscriber")
    suspend fun getSubscribedSubreddits(): SubredditListingResponse

    @GET("r/{subreddit}/hot")
    suspend fun getSubredditPosts(
        @Path("subreddit") subreddit: String,
        @Query("after") after: String,
        @Query("limit") perPage: Int
    ): PostListingResponse

    @GET("r/{subreddit}/about")
    suspend fun getSubreddit(
        @Path("subreddit") subreddit: String
    ): SubredditResponse

    @GET("by_id/{names}")
    suspend fun getPostsById(
        @Path("names") names: String
    ): PostListingResponse


    /**
     * @param targetFullName fullname (base36 string) of the post or comment
     * @param dir -1 to down vote, 0 to remove vote, 1 to up vote
     */
    @POST("/api/vote")
    suspend fun vote(
        @Query("id") targetFullName: String,
        @Query("dir") @IntRange(from = -1, to = 1) dir: Int
    )

//    @GET("r/{subreddit}/comments/{article}")
//    suspend fun getPostComments(
//        @Path("subreddit") subreddit: String,
//        @Path("article") article: String,
//        @Query("sort") sort: String = "top"
//    ): List<CommentListingResponse>
}