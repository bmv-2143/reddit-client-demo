package com.example.finalattestationreddit.presentation.bottom_navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.finalattestationreddit.data.NetworkError
import com.example.finalattestationreddit.data.NetworkError.ForbiddenApiRateExceeded
import com.example.finalattestationreddit.data.NetworkError.Unauthorized
import com.example.finalattestationreddit.databinding.ActivityBottomNavigationBinding
import com.example.finalattestationreddit.presentation.authorization.AuthorizationActivity
import com.example.finalattestationreddit.presentation.utils.SnackbarFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding
    private lateinit var navController: NavController

    private val viewModel by viewModels<BottomNavigationActivityViewModel>()

    @Inject
    lateinit var snackbarFactory: SnackbarFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavController()
        setupBottomNavView()
        observerNetworkErrors()
//        setupActionBarWithNavController()
    }

    private fun initNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_bottom_navigation)
                as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomNavView() {
        val navView: BottomNavigationView = binding.activityBottomNavNavView
//        navView.setupWithNavController(navController)

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
                viewModel.networkErrorsFlow.collect {  event ->
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
            is ForbiddenApiRateExceeded -> {
                snackbarFactory.showWarningSnackbar(
                    binding.root, "API rate limit exceeded!" // todo: hardcoded string
                )
            }

            is Unauthorized -> {
                startActivity(AuthorizationActivity.createIntent(this))
                finish()
            }

            else -> {
                snackbarFactory.showErrorSnackbar(binding.root, error.message)
            }
        }
    }

    private fun makePoppingUpToRootNaveOptions(): NavOptions {
        return NavOptions.Builder()
            .setPopUpTo(R.id.mobile_navigation, true)
            .build()
    }

//    private fun setupActionBarWithNavController() {
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_subreddits,
//                R.id.navigation_favorites,
//                R.id.navigation_user_profile
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BottomNavigationActivity::class.java)
        }
    }
}