package com.example.finalattestationreddit.presentation.authorization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.finalattestationreddit.data.network.AuthQuery.Companion.VAL_REDIRECT_URI
import com.example.finalattestationreddit.log.TAG
import com.example.unsplashattestationproject.databinding.ActivityMainBinding


class AuthorizationActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthorizationActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setButtonListener()
        handleIntent(intent)
    }

    private fun setButtonListener() {
        binding.button.setOnClickListener {
            openChromeTabForAuthentication()
        }
    }

    private fun openChromeTabForAuthentication() {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(this@AuthorizationActivity, viewModel.getAuthorizationRequestUri())
    }


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
            if (host != INTENT_FILTER_DATA_HOST_AUTH)
                return

            if (viewModel.hasValidResponseState(deepLinkData)) {
                Log.d(TAG, "${::handleIntent}: hasValidResponseState")
                Log.e(
                    TAG,
                    "${::handleIntent}: access_token: ${viewModel.getAccessToken(deepLinkData)}"
                )
            } else {
                Log.e(
                    TAG,
                    "${::handleIntent}: SECURITY ERROR: response state doesn't match request state"
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    companion object {
        const val INTENT_FILTER_DATA_HOST_AUTH = "auth"
    }
}

