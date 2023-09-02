package com.example.finalattestationreddit.data.repositories

import android.content.SharedPreferences
import com.example.finalattestationreddit.di.TokenProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenProvider {

    init {
        loadAccessToken()
    }

    private var accessToken: String = ""

    fun hasAccessToken(): Boolean = getToken().isNotEmpty()

    private fun loadAccessToken() {
        accessToken = sharedPreferences.getString(PREFS_KEY_ACCESS_TOKEN, "") ?: ""
    }

    internal fun saveAccessToken(accessToken: String) {
        this.accessToken = accessToken

        val editor = sharedPreferences.edit()
        editor.putString(PREFS_KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    @SuppressLint("ApplySharedPref")
    internal fun removeAccessTokenSync() {
        accessToken = ""

        val editor = sharedPreferences.edit()
        editor.remove(PREFS_KEY_ACCESS_TOKEN)
        editor.commit()
    }

    override fun getToken(): String {
        if (accessToken.isEmpty()) {
            loadAccessToken()
        }
        return accessToken
    }

    companion object {

        const val PREFS_KEY_ACCESS_TOKEN = "access_token"

    }

}