package com.example.finalattestationreddit.data.model.dto.subreddit

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class SubredditData(

    val id: String,
    val title: String,
    @Json(name="display_name") val displayName: String,
    @Json(name="display_name_prefixed") val displayNamePrefixed: String,
    val subscribers: Int?,
    @Json(name="user_is_subscriber") val userIsSubscriber: Boolean?,
    @Json(name="public_description") val publicDescription: String?,
    val url: String,
    @Json(name="community_icon") val communityIcon: String,

) : Parcelable