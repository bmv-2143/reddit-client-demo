package com.example.finalattestationreddit.data.mappers

import com.example.finalattestationreddit.data.dto.ListingData
import com.example.finalattestationreddit.data.dto.ListingResponse
import com.example.finalattestationreddit.data.dto.SubredditData


fun ListingResponse.toSubredditDataList() : List<SubredditData> =
    this.data.toSubredditDataList()

// todo: should be here?  in dto?
fun ListingData.toSubredditDataList(): List<SubredditData> =
    this.children.map { it.data }
