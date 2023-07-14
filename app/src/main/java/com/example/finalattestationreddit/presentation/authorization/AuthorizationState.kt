package com.example.finalattestationreddit.presentation.authorization

sealed class AuthorizationState {

    object NotStarted : AuthorizationState()
    object Success : AuthorizationState()
    object SecurityErrorResponseStateMismatch : AuthorizationState()
    object Failed : AuthorizationState()

}
