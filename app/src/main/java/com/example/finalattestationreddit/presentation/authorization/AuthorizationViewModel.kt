package com.example.finalattestationreddit.presentation.authorization

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalattestationreddit.data.repositories.LocalRepository
import com.example.finalattestationreddit.data.repositories.RedditRepository
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Failed
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Idle
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.SecurityErrorResponseStateMismatch
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val redditRepository: RedditRepository
) : ViewModel() {

    internal val networkErrorsFlow = redditRepository.networkErrorsFlow

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

    internal fun isOnboardingShowed(): Boolean = localRepository.isOnboardingShowed()

    internal fun saveOnboardingShowedStatus() {
        localRepository.saveOnboardingShowedStatus()
    }

    private var _authorizationState = MutableStateFlow<AuthorizationState>(Idle)
    internal val authorizationState = _authorizationState.asStateFlow()

    internal fun isUserAuthorized(): Boolean = redditRepository.hasAccessToken()


    internal fun handleAuthResponseUri(authResponse: Uri) {
        viewModelScope.launch {
            if (!hasValidResponseState(authResponse)) {
                redditRepository.removeAccessTokenSync()
                _authorizationState.emit(SecurityErrorResponseStateMismatch)
                return@launch
            }

            val accessToken = authRequest.extractAccessToken(authResponse)
            updateAuthorizationState(accessToken)
        }
    }

    private fun hasValidResponseState(uri: Uri): Boolean =
        authRequest.doesResponseStateMatchRequestState(uri)

    private suspend fun updateAuthorizationState(accessToken: String?) {
        if (!accessToken.isNullOrEmpty()) {
            redditRepository.saveAccessToken(accessToken)
            _authorizationState.emit(Success)
        } else {
            Log.e(TAG, "${::updateAuthorizationState}: access token is null or empty")
            redditRepository.removeAccessTokenSync()
            _authorizationState.emit(Failed)
        }
    }

}