package com.example.finalattestationreddit.data.errors

sealed class NetworkError(val message: String) {

    class ForbiddenApiRateExceeded(message: String) : NetworkError(message)
    class Unauthorized(message: String) : NetworkError(message)
    class HttpError(message: String) : NetworkError(message)
    class NoInternetConnection(message: String) : NetworkError(message)

}