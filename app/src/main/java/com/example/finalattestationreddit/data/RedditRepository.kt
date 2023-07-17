package com.example.finalattestationreddit.data

import android.content.SharedPreferences
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.finalattestationreddit.data.dto.SubredditData
import com.example.finalattestationreddit.data.pagingsource.GetSubredditsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE = 30
const val PREFETCH_DISTANCE =  PAGE_SIZE / 3

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

    internal fun getNewSubreddits(): Flow<PagingData<SubredditData>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { GetSubredditsPagingSource(redditNetworkDataSource) }
        ).flow
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