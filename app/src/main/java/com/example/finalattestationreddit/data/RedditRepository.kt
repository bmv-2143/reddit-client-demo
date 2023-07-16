package com.example.finalattestationreddit.data

import android.content.SharedPreferences
import com.example.finalattestationreddit.data.dto.SubredditData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedditRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val redditNetworkDataSource: RedditNetworkDataSource
) {

    init {
        redditAccessToken = sharedPreferences.getString(PREFS_KEY_ACCESS_TOKEN, "") ?: ""
    }

    fun hasAccessToken(): Boolean = redditAccessToken.isNotEmpty()

    internal fun saveAccessToken(accessToken: String) {
        cacheToken(accessToken)

        val editor = sharedPreferences.edit()
        editor.putString(PREFS_KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    private fun cacheToken(accessToken: String) {
        redditAccessToken = accessToken
    }

    internal fun removeAccessToken() {
        val editor = sharedPreferences.edit()
        editor.remove(PREFS_KEY_ACCESS_TOKEN)
        editor.apply()
    }

    suspend fun getNewSubreddits(): List<SubredditData> {
        return redditNetworkDataSource.getNewSubreddits()
    }

    suspend fun getPopularSubreddits(): List<SubredditData> {
        return redditNetworkDataSource.getPopularSubreddits()
    }


    companion object {

        const val PREFS_KEY_ACCESS_TOKEN = "access_token"

        var redditAccessToken: String = ""
            private set
    }

}