package com.example.finalattestationreddit.presentation.authorization

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.example.finalattestationreddit.log.TAG
import com.example.unsplashattestationproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthorizationViewModel by viewModels()

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
        intent.launchUrl(this@AuthorizationActivity, viewModel.makeAuthorizationRequestUri())
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action != Intent.ACTION_VIEW)
            return

        val deepLinkResponseUri = intent.data ?: return

        if (deepLinkResponseUri.host != INTENT_FILTER_DATA_HOST_AUTH)
            return

        handleAuthResponseUri(deepLinkResponseUri)
    }

    private fun handleAuthResponseUri(authResponse: Uri) =
        if (viewModel.hasValidResponseState(authResponse)) {
            Log.e(
                TAG,
                "${::handleIntent}: access_token: ${viewModel.getAccessToken(authResponse)}"
            )

            // TODO: continue to the next screen

        } else {

            // TODO: notify user

            Log.e(
                TAG,
                "${::handleIntent}: SECURITY ERROR: response state doesn't match request state"
            )
        }

    companion object {
        const val INTENT_FILTER_DATA_HOST_AUTH = "auth"
    }
}

