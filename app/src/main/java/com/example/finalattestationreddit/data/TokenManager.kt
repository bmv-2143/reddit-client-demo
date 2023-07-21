package com.example.finalattestationreddit.data

import android.content.SharedPreferences
import android.util.Log
import com.example.finalattestationreddit.di.TokenProvider
import com.example.finalattestationreddit.log.TAG
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenProvider {

    init {
        loadAccessToken()
    }

    private var accessToken: String = ""

    fun hasAccessToken(): Boolean = accessToken.isNotEmpty()

    private fun loadAccessToken() {
        accessToken = sharedPreferences.getString(PREFS_KEY_ACCESS_TOKEN, "") ?: ""
    }

    internal fun saveAccessToken(accessToken: String) {
        this.accessToken = accessToken

        val editor = sharedPreferences.edit()
        editor.putString(PREFS_KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    internal fun removeAccessToken() {
        accessToken = ""

        val editor = sharedPreferences.edit()
        editor.remove(PREFS_KEY_ACCESS_TOKEN)
        editor.apply()
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