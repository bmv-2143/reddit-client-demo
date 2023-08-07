package com.example.finalattestationreddit.data.network

import android.support.annotation.IntRange
import com.example.finalattestationreddit.data.dto.comment.CommentListingResponse
import com.example.finalattestationreddit.data.dto.post.PostListingResponse
import com.example.finalattestationreddit.data.dto.subreddit.SubredditListingResponse
import com.example.finalattestationreddit.data.dto.subreddit.SubredditResponse
import com.example.finalattestationreddit.data.dto.user.AddFriendRequest
import com.example.finalattestationreddit.data.dto.user.FriendsResponse
import com.example.finalattestationreddit.data.dto.user.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("r/{subreddit}/comments/{postName}")
    suspend fun getPostComments(
        @Path("subreddit") subreddit: String,
        @Path("postName") postName: String,
        @Query("sort") sort: String = "top",
        @Query("depth") depth: Int? = null,
        @Query("limit") limit: Int? = null,

        // todo: do I need that?
        @Query("more") more: Boolean = false,
        @Query("threaded") threaded: Boolean = false,

        ): List<CommentListingResponse>

    @GET("/api/info")
    suspend fun getCommentById(
        @Query("id") commentId: String
    ): CommentListingResponse

    @GET("/user/{username}/about")
    suspend fun getUser(
        @Path("username") username: String
    ): UserResponse

    @GET("/user/{username}/submitted")
    suspend fun getUserPosts(
        @Path("username") username: String,
        @Query("after") after: String,
        @Query("limit") limit: Int
    ): PostListingResponse

    @GET("/user/{username}/submitted")
    suspend fun getUserPostsAll(
        @Path("username") username: String,
        @Query("limit") limit: Int = DEFAULT_MAX_USER_POSTS_REQUEST_LIMIT
    ): PostListingResponse

    @PUT("/api/v1/me/friends/{username}")
    suspend fun addFriend(
        @Path("username") username: String,
        @Body body: AddFriendRequest = AddFriendRequest(username)
    )

    @DELETE("/api/v1/me/friends/{username}")
    suspend fun removeFriend(
        @Path("username") username: String
    )

    @GET("/api/v1/me/friends")
    suspend fun getFriends(): FriendsResponse

    companion object {
        const val DEFAULT_MAX_USER_POSTS_REQUEST_LIMIT = 10_000
    }
}