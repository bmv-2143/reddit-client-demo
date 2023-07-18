package com.example.finalattestationreddit.data.dto.subreddit

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class SubredditData(

    @Json(name="id") val id: String,
    @Json(name="title") val title: String,
    @Json(name="display_name") val displayName: String,
    @Json(name="display_name_prefixed") val displayNamePrefixed: String,
    @Json(name="subscribers") val subscribers: Int,
    @Json(name="user_is_subscriber") val userIsSubscriber: Boolean,
    @Json(name="public_description") val publicDescription: String

) : Parcelable