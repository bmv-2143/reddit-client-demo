package com.example.finalattestationreddit.presentation.authorization

import android.net.Uri
import android.util.Log
import com.example.finalattestationreddit.BuildConfig
import com.example.finalattestationreddit.data.network.AuthQuery
import com.example.finalattestationreddit.log.TAG

class AuthorizationRequest(val requestState: String = AuthQuery.generateAuthRequestState()) {

    internal val browserAuthUri : Uri =
        Uri.parse(AuthQuery.AUTH_URL)
            .buildUpon()

            // Authorization (Implicit grant flow) (only for "installed apps")
            // https://github.com/reddit-archive/reddit/wiki/OAuth2#authorization-implicit-grant-flow
            .appendQueryParameter(AuthQuery.PARAM_CLIENT_ID, BuildConfig.REDDIT_CLIENT_ID)
            .appendQueryParameter(AuthQuery.PARAM_RESPONSE_TYPE, AuthQuery.VAL_RESPONSE_TYPE)
            .appendQueryParameter(AuthQuery.PARAM_STATE, requestState)
            .appendQueryParameter(AuthQuery.PARAM_REDIRECT_URI, AuthQuery.VAL_REDIRECT_URI)
            .appendQueryParameter(AuthQuery.PARAM_SCOPE, AuthQuery.VAL_SCOPE)
            .build()


    internal fun extractAccessToken(authResponse: Uri) : String? =
        extractFragmentParam(authResponse, AuthQuery.URI_FRAGMENT_PART_ACCESS_TOKEN)

    private fun extractResponseState(authResponse: Uri) : String? =
        extractFragmentParam(authResponse, AuthQuery.PARAM_STATE)

    private fun extractFragmentParam(authResponse: Uri, paramName : String) : String? {
        val fragment = authResponse.fragment

        return if (fragment == null) {
            Log.e(TAG, "${::extractAccessToken} : fragment response part is null")
            null
        } else {
            val paramsMap = extractUriFragmentParams(fragment)
            paramsMap[paramName]
        }.also {
            Log.e(TAG, "${::extractAccessToken} : $paramName: $it")
        }
    }

    private fun extractUriFragmentParams(uriFragment: String): Map<String, String> {
        val params = uriFragment.split("&")
        return params.map { it.split("=") }.associate { it[0] to it[1] }
    }

    internal fun doesResponseStateMatchRequestState(authResponse: Uri) : Boolean {
        val responseState = extractResponseState(authResponse)
        Log.e(TAG, "$responseState ::: $requestState")
        return responseState == requestState
    }

}