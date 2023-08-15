package com.example.finalattestationreddit.data.pagingsource

import com.example.finalattestationreddit.data.model.dto.post.PostData
import com.example.finalattestationreddit.data.model.dto.post.PostListingData
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditListingData


fun SubredditListingData.toSubredditDataList(): List<SubredditData> = this.children.map { it.data }

fun PostListingData.toPostDataList(): List<PostData> = this.children
