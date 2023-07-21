package com.example.finalattestationreddit.data

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.finalattestationreddit.di.TokenProvider
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenProvider {

    init {
        loadAccessToken()
    }

    fun hasAccessToken(): Boolean = accessToken.isNotEmpty()

    private fun loadAccessToken() {
        accessToken = sharedPreferences.getString(PREFS_KEY_ACCESS_TOKEN, "") ?: ""
    }

    internal fun saveAccessToken(accessToken: String) {
        cacheToken(accessToken)

        val editor = sharedPreferences.edit()
        editor.putString(PREFS_KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    private fun cacheToken(token: String) {
        accessToken = token
    }

    @SuppressLint("ApplySharedPref")
    internal fun removeAccessTokenSync() {
        accessToken = ""

        val editor = sharedPreferences.edit()
        editor.remove(PREFS_KEY_ACCESS_TOKEN)
        editor.commit()  // synchronous operation required
    }

    override fun getToken(): String {
        Log.e(TAG, "getToken: $accessToken")
        if (accessToken.isEmpty()) {
            loadAccessToken()
        }
        return accessToken
    }

    companion object {

        const val PREFS_KEY_ACCESS_TOKEN = "access_token"

        var accessToken: String = ""
            private set
    }

}