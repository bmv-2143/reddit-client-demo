package com.example.finalattestationreddit.presentation.authorization

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.finalattestationreddit.data.LocalRepository
import com.example.finalattestationreddit.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private val authRequest: AuthorizationRequest by lazy {
        makeAuthRequest()
    }

    internal fun getCachingAuthorizationRequestUri(): Uri {
//        initAuthRequestIfRequired()
        return authRequest.browserAuthUri
    }

    internal fun hasValidResponseState(uri: Uri): Boolean {
//        initAuthRequestIfRequired()
        Log.e(TAG, "hasValidResponseState: authRequest = $authRequest")
        return authRequest.doesResponseStateMatchRequestState(uri)
    }

//    private fun initAuthRequestIfRequired() {
//        if (authRequest == null) {
//            val requestState = localRepository.getAuthRequestState()
//            authRequest = if (requestState != null) {
//                AuthorizationRequest(requestState)
//            } else {
//                val request = AuthorizationRequest()
//                localRepository.saveAuthRequestState(request.requestState)
//                request
//            }
//        }
//    }

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
        authRequest?.extractAccessToken(uri)

        companion object {
        const val PREF_KEY_REQUEST_STATE = "requestState"
    }
}