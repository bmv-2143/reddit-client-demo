package com.example.finalattestationreddit.data.mappers

import com.example.finalattestationreddit.data.dto.subreddit.SubredditListingData
import com.example.finalattestationreddit.data.dto.subreddit.SubredditListingResponse
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData


fun SubredditListingResponse.toSubredditDataList() : List<SubredditData> =
    this.data.toSubredditDataList()

// todo: should be here?  in dto?
fun SubredditListingData.toSubredditDataList(): List<SubredditData> =
    this.children.map { it.data }
