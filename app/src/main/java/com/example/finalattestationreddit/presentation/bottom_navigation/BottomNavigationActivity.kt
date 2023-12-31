package com.example.finalattestationreddit.presentation.bottom_navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.model.errors.NetworkError
import com.example.finalattestationreddit.data.model.errors.NetworkError.ForbiddenApiRateExceeded
import com.example.finalattestationreddit.data.model.errors.NetworkError.Unauthorized
import com.example.finalattestationreddit.databinding.ActivityBottomNavigationBinding
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.authorization.AuthorizationActivity
import com.example.finalattestationreddit.presentation.utils.SnackbarFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding
    private lateinit var navController: NavController

    private val viewModel by viewModels<BottomNavigationViewModel>()

    @Inject
    lateinit var snackbarFactory: SnackbarFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavController()
        setupBottomNavView()
        observerNetworkErrors()
        observeLogoutEvents()
    }

    private fun initNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_bottom_navigation)
                as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomNavView() {
        val navView: BottomNavigationView = binding.activityBottomNavNavView

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_subreddits -> {
                    navController.navigate(R.id.navigation_subreddits)
                    true
                }

                R.id.navigation_favorites -> {
                    navController.navigate(
                        R.id.navigation_favorites, null, makePoppingUpToRootNaveOptions()
                    )
                    true
                }

                R.id.navigation_user_profile -> {
                    navController.navigate(
                        R.id.navigation_user_profile, null, makePoppingUpToRootNaveOptions()
                    )
                    true
                }

                else -> false
            }
        }
    }

    private fun observerNetworkErrors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.networkErrorsFlow.collect { event ->
                    if (!event.hasBeenConsumed) {
                        val error = event.peekContent()
                        handleNetworkError(error)
                    }
                }
            }
        }
    }

    private fun handleNetworkError(error: NetworkError) {
        when (error) {
            ForbiddenApiRateExceeded -> {
                snackbarFactory.showWarningSnackbar(
                    binding.root,
                    getString(R.string.activity_bottom_navigation_error_api_rate_limit_exceeded)
                )
            }

            Unauthorized -> {
                openAuthorizationScreenAndCloseSelf()
            }

            NetworkError.NoInternetConnection -> {
                snackbarFactory.showWarningSnackbar(
                    binding.root,
                    getString(R.string.activity_bottom_navigation_error_no_internet_connection)
                )
            }

            else -> Log.e(TAG, "${::handleNetworkError.name}: $error")
        }
    }

    private fun openAuthorizationScreenAndCloseSelf() {
        startActivity(AuthorizationActivity.createIntent(this))
        finish()
    }

    private fun makePoppingUpToRootNaveOptions(): NavOptions {
        return NavOptions.Builder()
            .setPopUpTo(R.id.mobile_navigation, true)
            .build()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeLogoutEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.logoutEvents.collectLatest {
                    openAuthorizationScreenAndCloseSelf()
                }
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BottomNavigationActivity::class.java)
        }
    }
}