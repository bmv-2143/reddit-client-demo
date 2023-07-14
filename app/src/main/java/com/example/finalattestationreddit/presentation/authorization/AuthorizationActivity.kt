package com.example.finalattestationreddit.presentation.authorization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Failed
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Idle
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.SecurityErrorResponseStateMismatch
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Success
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationActivity
import com.example.finalattestationreddit.presentation.onboarding.OnboardingActivity
import com.example.finalattestationreddit.presentation.utils.SnackbarFactory
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.ActivityAuthorizationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {

    private val binding: ActivityAuthorizationBinding by lazy {
        ActivityAuthorizationBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeAuthorizationState()
        openNextScreenIfRequired()
        setButtonListener()
        handleIntent(intent)
    }

    private fun openNextScreenIfRequired() {
        if (!viewModel.isOnboardingShowed()) {
            openOnboardingActivity()
        } else if (viewModel.isUserAuthorized()) {
            proceedToBottomNavActivity()
        }
    }

    private fun openOnboardingActivity() {
        startActivity(OnboardingActivity.createIntent(this))
        viewModel.saveOnboardingShowedStatus()
    }

    private fun setButtonListener() {
        binding.activityAuthorizationBtnLogin.setOnClickListener {
            openChromeTabForAuthentication()
        }
    }

    private fun openChromeTabForAuthentication() {
        val intent = CustomTabsIntent.Builder().build()
        intent.launchUrl(
            this@AuthorizationActivity,
            viewModel.getCachingAuthorizationRequestUri()
        )
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

        viewModel.handleAuthResponseUri(deepLinkResponseUri)
    }


    private fun observeAuthorizationState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authorizationState
                    .collect { isAuthorizationSuccess ->
                        handleAuthorizationState(isAuthorizationSuccess)
                    }
            }
        }
    }

    private fun handleAuthorizationState(isAuthorizationSuccess: AuthorizationState) {
        when (isAuthorizationSuccess) {
            is Idle -> return

            is Success -> proceedToBottomNavActivity()

            is Failed -> {
                Log.e(TAG, "Authorization failed: user declined")
                Toast.makeText(
                    applicationContext,
                    getString(R.string.activity_authorization_result_msg_failed), Toast.LENGTH_SHORT
                ).show()
            }

            is SecurityErrorResponseStateMismatch -> {
                Log.e(
                    TAG,
                    "Authorization failed: security error: request-response state mismatch"
                )
                SnackbarFactory(applicationContext).showErrorSnackbar(
                    binding.root,
                    getString(R.string.activity_authorization_result_security_error)
                )
            }
        }
    }

    private fun proceedToBottomNavActivity() {
        startActivity(BottomNavigationActivity.createIntent(this@AuthorizationActivity))
        finish()
    }


    companion object {
        const val INTENT_FILTER_DATA_HOST_AUTH = "auth"
    }
}

