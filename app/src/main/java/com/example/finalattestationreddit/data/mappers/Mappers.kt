package com.example.finalattestationreddit.data.mappers

import com.example.finalattestationreddit.data.dto.subreddit.ListingData
import com.example.finalattestationreddit.data.dto.subreddit.ListingResponse
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData


fun ListingResponse.toSubredditDataList() : List<SubredditData> =
    this.data.toSubredditDataList()

// todo: should be here?  in dto?
fun ListingData.toSubredditDataList(): List<SubredditData> =
    this.children.map { it.data }
