package com.example.finalattestationreddit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.finalattestationreddit.AuthQuery.Companion.PARAM_CLIENT_ID
import com.example.finalattestationreddit.AuthQuery.Companion.PARAM_REDIRECT_URI
import com.example.finalattestationreddit.AuthQuery.Companion.PARAM_RESPONSE_TYPE
import com.example.finalattestationreddit.AuthQuery.Companion.PARAM_SCOPE
import com.example.finalattestationreddit.AuthQuery.Companion.PARAM_STATE
import com.example.finalattestationreddit.AuthQuery.Companion.URI_FRAGMENT_PART_ACCESS_TOKEN
import com.example.finalattestationreddit.AuthQuery.Companion.VAL_REDIRECT_URI
import com.example.finalattestationreddit.AuthQuery.Companion.VAL_RESPONSE_TYPE
import com.example.finalattestationreddit.AuthQuery.Companion.VAL_SCOPE
import com.example.finalattestationreddit.AuthQuery.Companion.generateAuthRequestState
import com.example.finalattestationreddit.log.TAG
import com.example.unsplashattestationproject.BuildConfig
import com.example.unsplashattestationproject.databinding.ActivityMainBinding
import java.util.UUID


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setButtonListener()
        handleIntent(intent) // todo: do I need it here?
    }

    private fun setButtonListener() {
        binding.button.setOnClickListener {
            openChromeTabForAuthentication()
        }
    }

    var requestState: String = generateAuthRequestState()

    private fun openChromeTabForAuthentication() {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this@MainActivity, composeBrowserAuthUrl())
    }

    private fun composeBrowserAuthUrl(): Uri =
        Uri.parse(AuthQuery.AUTH_URL)
            .buildUpon()

            // Authorization (Implicit grant flow) (only for "installed apps")
            // https://github.com/reddit-archive/reddit/wiki/OAuth2#authorization-implicit-grant-flow
            .appendQueryParameter(PARAM_CLIENT_ID, BuildConfig.REDDIT_CLIENT_ID)
            .appendQueryParameter(PARAM_RESPONSE_TYPE, VAL_RESPONSE_TYPE)
            .appendQueryParameter(PARAM_STATE, requestState)
            .appendQueryParameter(PARAM_REDIRECT_URI, VAL_REDIRECT_URI)
            .appendQueryParameter(PARAM_SCOPE, VAL_SCOPE)
            .build()


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = intent?.data
        if (uri != null && uri.toString().startsWith(VAL_REDIRECT_URI)) {
            val token = uri.getQueryParameter("access_token")

            // Use the token for further API calls
        }

        intent?.let {
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW)
            return

        val deepLinkData = intent.data ?: return

        deepLinkData.host?.let { host ->
            if (host == INTENT_FILTER_DATA_HOST_AUTH) {
                extractParamsFromAuthRequest(deepLinkData)
            }
        }
    }


    private fun extractParamsFromAuthRequest(uri: Uri) {

        val fragment = uri.fragment
        if (fragment != null) {
            val params = fragment.split("&")
            val paramMap = params.map { it.split("=") }.associate { it[0] to it[1] }
            val accessToken = paramMap[URI_FRAGMENT_PART_ACCESS_TOKEN]
            val state = paramMap[PARAM_STATE]

            Log.e(TAG, "authorize: access_token: $accessToken")
            Log.e(TAG, "authorize: state: $state")
        }
    }

    companion object {
        const val INTENT_FILTER_DATA_HOST_AUTH = "auth"
    }
}

class AuthQuery {

    /*
     * Sample Auth URL for "Authorization (Implicit grant flow)" (for Reddit "installed apps" type)
     * https://github.com/reddit-archive/reddit/wiki/OAuth2#authorization-implicit-grant-flow
     *
     * https://www.reddit.com/api/v1/authorize
     *      ?client_id=CLIENT_ID
     *      &response_type=TYPE
     *      &state=RANDOM_STRING
     *      &redirect_uri=URI
     *      &scope=SCOPE_STRING
     */

    companion object {
        const val AUTH_URL = "https://www.reddit.com/api/v1/authorize"

        const val PARAM_CLIENT_ID: String = "client_id"

        const val PARAM_RESPONSE_TYPE: String = "response_type"
        const val VAL_RESPONSE_TYPE: String =
            "token"                 // todo: this should not be code, use another flow

        const val PARAM_REDIRECT_URI: String = "redirect_uri"
        const val VAL_REDIRECT_URI: String = "com.example.finalattestationreddit://auth"

        const val PARAM_STATE: String = "state"

        const val PARAM_SCOPE: String = "scope"
        const val VAL_SCOPE: String =         // todo: request only the scopes you need
            "identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits " +
                    "privatemessages read report save submit subscribe vote wikiedit wikiread"


        const val URI_FRAGMENT_PART_ACCESS_TOKEN = "access_token"

        internal fun generateAuthRequestState(): String {
            return UUID.randomUUID().toString()
        }
    }
}


/*

There was an error trying to connect with your reddit account
You got here because a third-party application wanted to authorize connecting with your reddit account. However an unknown error occurred, please see details below. Please try refreshing the page or coming back later.
Error: Error fetching oauth client
Error: Error fetching oauth client
at requestOauthApp (/src/packages/applications/monolith/bin/index.cjs:145809:16)
at async Proxy.renderAfterQuery (/src/packages/applications/monolith/bin/index.cjs:187682:26)
at async /src/packages/applications/monolith/bin/index.cjs:142903:21
at async withLocalSpanGeneratorAsync (/src/packages/applications/monolith/bin/index.cjs:136451:18)
at async Proxy.<anonymous> (/src/packages/applications/monolith/bin/index.cjs:142901:17)
at async renderFeature (/src/packages/applications/monolith/bin/index.cjs:143173:21)
at async renderFeatureTree (/src/packages/applications/monolith/bin/index.cjs:143318:5)
at async StreamingRenderer.renderAsGenerator (/src/packages/applications/monolith/bin/index.cjs:143413:13)


handleIntent: URI: com.example.finalattestationreddit://auth#access_token=eyJhbGciOiJSUzI1NiIsImtpZCI6IlNIQTI1NjpzS3dsMnlsV0VtMjVmcXhwTU40cWY4MXE2OWFFdWFyMnpLMUdhVGxjdWNZIiwidHlwIjoiSldUIn0.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjg5MjU4ODIzLjA4ODUxNiwiaWF0IjoxNjg5MTcyNDIzLjA4ODUxNiwianRpIjoidlpDVzVlenB2S0lteFFqZXRMWktJUlh0SU9XMEJRIiwiY2lkIjoiZDJ1Z3JxdTRUWXlRdy1MMEVzZTJ2ZyIsImxpZCI6InQyX2lwc3gzb2FwIiwiYWlkIjoidDJfaXBzeDNvYXAiLCJsY2EiOjE2NDI1MjA1OTUwNjksInNjcCI6ImVKeEVqVUVPZzBBSVJlX3kxOXlvNldKRzBKSTZZZ0J0dkgwemF0TWRmQjdfUGZEUnR3cHJnaEJsRjlDWnVCUUdvUm4zRFlRYjJTMDcwbzdZcWd1elp2VEhyY2JnV3Z0cGRkMUxTcE9JTWtsY0pZTXRvMDRnX0h0bm02NWh0Y2diRy1laWZsS3JlZGY5Z3RocU9fM0tzcVRtQWNKTEk4MFBQTDhCQUFEX184SEZReGMiLCJmbG8iOjd9.e89pkq44LhjJCRMBkul87KMM5fIQ85pdr0bDrSSv4dB1K2knqHU4zKqV06ccrG-eKQu7fBXfZ_ZbJhxo7VdW1pmxEC6oXu168PBmpGPtFjf7_ZAuVpD_xYXoN2VISLKHrNJa5ChCimb-x7Nb_g8toT20BocPFAvZUALIhIlXdEYcfYg6E31gDR07SGDm-K04LRO80qTUiEZESNwj5gBVQ0PZz2kiqyhEZ9gBukSegcvTWv_rQs9OtiDpGKf33tzxUxRgSCI1Pdc1EwMu9rfaG2Myij4PSL2PlBbIdHWsOs2OFMNRCew4-L9cd32IxTw4084tEjbt0S1WyNFww28Uzw&token_type=bearer&state=48200bcd-0f1d-40cc-8b8f-5e69959e7df6&expires_in=86400&scope=wikiedit+save+wikiread+modwiki+edit+vote+mysubreddits+subscribe+privatemessages+modconfig+read+modlog+modposts+modflair+report+flair+submit+identity+history


 */