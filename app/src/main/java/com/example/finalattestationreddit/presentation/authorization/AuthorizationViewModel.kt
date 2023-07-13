package com.example.finalattestationreddit.presentation.authorization

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.finalattestationreddit.data.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private val authRequest: AuthorizationRequest = makeAuthRequest()

    internal fun getCachingAuthorizationRequestUri(): Uri = authRequest.browserAuthUri

    internal fun hasValidResponseState(uri: Uri): Boolean =
        authRequest.doesResponseStateMatchRequestState(uri)

    private fun makeAuthRequest(): AuthorizationRequest {
        val requestState = localRepository.getAuthRequestState()
        return if (requestState != null) {
            AuthorizationRequest(requestState)
        } else {
            val request = AuthorizationRequest()
            localRepository.saveAuthRequestState(request.requestState)
            request
        }
    }

    internal fun getAccessToken(uri: Uri): String? =
        authRequest.extractAccessToken(uri)

}