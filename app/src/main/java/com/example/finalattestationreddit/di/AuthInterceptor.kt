package com.example.finalattestationreddit.di

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION_HEADER, "$BEARER_PREFIX ${tokenProvider.getToken()}")
            .build()
        return chain.proceed(request)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_PREFIX = "Bearer"
    }

}