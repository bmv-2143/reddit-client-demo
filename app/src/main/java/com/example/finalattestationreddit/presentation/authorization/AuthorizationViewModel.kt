package com.example.finalattestationreddit.presentation.authorization

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.LocalRepository
import com.example.finalattestationreddit.data.RedditRepository
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val redditRepository: RedditRepository
) : ViewModel() {

    private val authRequest: AuthorizationRequest = makeAuthRequest()

    internal fun getCachingAuthorizationRequestUri(): Uri = authRequest.browserAuthUri


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

//    internal fun getAccessToken(uri: Uri): String? =
//        authRequest.extractAccessToken(uri)

    internal fun isOnboardingShowed(): Boolean = localRepository.isOnboardingShowed()

    fun saveOnboardingShowedStatus() {
        localRepository.saveOnboardingShowedStatus()
    }

    private var _authorizationState = MutableStateFlow<AuthorizationState>(NotStarted)
    val authorizationState = _authorizationState.asStateFlow()

    fun isUserAuthorized(): Boolean = redditRepository.hasAccessToken()


    internal fun handleAuthResponseUri(authResponse: Uri) {
        viewModelScope.launch {

            if (!hasValidResponseState(authResponse)) {
                _authorizationState.emit(SecurityErrorResponseStateMismatch)
                return@launch
            }

            val accessToken = authRequest.extractAccessToken(authResponse)
            if (!accessToken.isNullOrEmpty()) {
                redditRepository.saveAccessToken(accessToken)
                _authorizationState.emit(Success)
            } else {
                Log.e(TAG, "handleAuthResponseUri: access token is null or empty")
                _authorizationState.emit(Failed)
            }
        }
    }

    private fun hasValidResponseState(uri: Uri): Boolean =
        authRequest.doesResponseStateMatchRequestState(uri)

}