package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.model.dto.post.PostData
import com.example.finalattestationreddit.data.model.dto.post.PostListingData
import com.example.finalattestationreddit.data.model.dto.post.PostListingResponse
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditListingData
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditListingResponse
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData


fun SubredditListingResponse.toSubredditDataList() : List<SubredditData> =
    this.data.toSubredditDataList()

// todo: should be here?  in dto?
fun SubredditListingData.toSubredditDataList(): List<SubredditData> =
    this.children.map { it.data }

fun PostListingResponse.toPostDataList() : List<PostData> =
    this.data.toPostDataList()

// todo: should be here?  in dto?
fun PostListingData.toPostDataList(): List<PostData> =
    this.children
