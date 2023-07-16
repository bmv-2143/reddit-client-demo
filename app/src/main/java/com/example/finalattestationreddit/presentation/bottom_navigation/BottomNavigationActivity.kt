package com.example.finalattestationreddit.presentation.bottom_navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.ActivityBottomNavigationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavController()
        setupBottomNavView()
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