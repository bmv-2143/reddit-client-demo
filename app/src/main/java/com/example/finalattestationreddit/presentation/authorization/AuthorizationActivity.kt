package com.example.finalattestationreddit.presentation.authorization

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.authorization.AuthorizationState.*
import com.example.finalattestationreddit.presentation.onboarding.OnboardingActivity
import com.example.finalattestationreddit.presentation.utils.SnackbarFactory
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
        }

        else if (viewModel.isUserAuthorized()) {
//            startActivity(Intent(this, BottomNavigationActivity::class.java))
//            finish()

            SnackbarFactory(applicationContext).showSnackbar(
                binding.root, "User was authorized previously", "button")

        }
    }

    private fun openOnboardingActivity() {
        startActivity(OnboardingActivity.createIntent(this))
        viewModel.saveOnboardingShowedStatus()
    }

    private fun setButtonListener() {
        binding.button.setOnClickListener {
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
//        logAndNotifyUser(isAuthorizationSuccess)

        when (isAuthorizationSuccess) {
            is Success -> {
//                //            startActivity(
////                BottomNavigationActivity
////                    .createIntent(this@AuthorizationActivity)
////            )
////            finish()


                SnackbarFactory(applicationContext).showWarningSnackbar(
                    binding.root,
                    "Authorization success.")

                Log.e(TAG, "Authorization success.")

            }
            is Failed -> {
                SnackbarFactory(applicationContext).showWarningSnackbar(
                    binding.root,
                    "Authorization failed. Try again later.")

                Log.e(TAG, "Authorization failed. Try again later.")

            }
            is SecurityErrorResponseStateMismatch -> {
                SnackbarFactory(applicationContext).showErrorSnackbar(
                    binding.root,
                    "Authorization security error.")

                Log.e(TAG, "Authorization security error.")
            }

            is NotStarted -> {


                Log.e(TAG, "Not Started.")}
        }
    }

    companion object {
        const val INTENT_FILTER_DATA_HOST_AUTH = "auth"
    }
}

