package com.example.finalattestationreddit.presentation.authorization

sealed class AuthorizationState {

    object Idle : AuthorizationState()
    object Success : AuthorizationState()
    object SecurityErrorResponseStateMismatch : AuthorizationState()
    object Failed : AuthorizationState()

}
