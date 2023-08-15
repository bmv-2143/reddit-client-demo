package com.example.finalattestationreddit.data.repositories

import android.content.SharedPreferences
import com.example.finalattestationreddit.data.PrefsKeys
import com.example.finalattestationreddit.data.PrefsKeys.Companion.IS_ONBOARDING_SHOWED
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository  @Inject constructor(
    private val sharedPreferences: SharedPreferences
)  {

    fun isOnboardingShowed(): Boolean = sharedPreferences.getBoolean(
        IS_ONBOARDING_SHOWED, false)

    fun saveOnboardingShowedStatus() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_ONBOARDING_SHOWED, true)
        editor.apply()
    }

    fun getAuthRequestState() : String? {
        return sharedPreferences.getString(PrefsKeys.AUTH_REQUEST_STATE, null)
    }

    fun saveAuthRequestState(authRequestState : String) {
        val editor = sharedPreferences.edit()
        editor.putString(PrefsKeys.AUTH_REQUEST_STATE, authRequestState)
        editor.apply()
    }

}