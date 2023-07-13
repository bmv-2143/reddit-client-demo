package com.example.finalattestationreddit.presentation.authorization

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val preferences: SharedPreferences, // todo: move to repository
) : ViewModel() {

    private var authRequest: AuthorizationRequest? = null

    private val REQUEST_STATE = "requestState"

    internal fun makeAuthorizationRequestUri(): Uri {
        if (authRequest == null) {
            authRequest = AuthorizationRequest()
            preferences.edit().putString(REQUEST_STATE, authRequest!!.requestState).apply()
        }
        return authRequest!!.browserAuthUri
    }

    internal fun hasValidResponseState(uri: Uri): Boolean {
        if (authRequest == null) {
            val requestState = preferences.getString(REQUEST_STATE, null)?.also {
                preferences.edit().remove(REQUEST_STATE).apply()
            }
            if (requestState != null) {
                authRequest = AuthorizationRequest(requestState)
            }
        }

        Log.e(TAG, "hasValidResponseState: authRequest = $authRequest")
        return authRequest?.doesResponseStateMatchRequestState(uri) ?: false
    }

    internal fun getAccessToken(uri: Uri): String? =
        authRequest?.extractAccessToken(uri)

    override fun onCleared() {
        super.onCleared()
        Log.e(TAG, "AuthorizationActivityViewModel onCleared")
    }
}