package com.example.finalattestationreddit.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ListingResponse(

    @Json(name="kind") val kind: String,
    @Json(name="data") val data: ListingData

)

