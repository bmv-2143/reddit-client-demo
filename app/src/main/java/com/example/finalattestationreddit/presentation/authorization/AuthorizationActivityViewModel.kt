package com.example.finalattestationreddit.presentation.authorization

import android.net.Uri
import androidx.lifecycle.ViewModel

class AuthorizationActivityViewModel : ViewModel() {

    private val authRequest : AuthorizationRequest by lazy { AuthorizationRequest() }

    fun getAuthorizationRequestUri() : Uri = authRequest.browserAuthUri

    fun hasValidResponseState(uri : Uri) : Boolean =
        authRequest.doesResponseStateMatchRequestState(uri)

    fun getAccessToken(uri : Uri) : String? =
        authRequest.extractAccessToken(uri)
}