package com.example.finalattestationreddit.presentation.authorization

import android.content.Context
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
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.ActivityAuthorizationBinding
import com.example.finalattestationreddit.data.NetworkError
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Failed
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Idle
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.SecurityErrorResponseStateMismatch
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.Success
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationActivity
import com.example.finalattestationreddit.presentation.onboarding.OnboardingActivity
import com.example.finalattestationreddit.presentation.utils.SnackbarFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AuthorizationActivity : AppCompatActivity() {

    private val binding: ActivityAuthorizationBinding by lazy {
        ActivityAuthorizationBinding.inflate(layoutInflater)
    }

    private val viewModel: AuthorizationViewModel by viewModels()

    @Inject
    lateinit var snackbarFactory: SnackbarFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observerNetworkErrors()
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

    private fun observerNetworkErrors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.networkErrorsFlow.collect { event ->
                    event.consumeContent()?.let { error ->
                        handleNetworkError(error)
                    }
                }
            }
        }
    }

    private fun handleNetworkError(error: NetworkError) {
        when (error) {
            is NetworkError.Unauthorized -> snackbarFactory.showErrorSnackbar(
                binding.root,
                getString(R.string.activity_authorization_network_error_unauthorized)
            )

            else -> Log.e(TAG, "Network error: $error")
        }
    }

    companion object {
        const val INTENT_FILTER_DATA_HOST_AUTH = "auth"

        fun createIntent(context: Context): Intent {
            return Intent(context, AuthorizationActivity::class.java)
        }
    }
}

