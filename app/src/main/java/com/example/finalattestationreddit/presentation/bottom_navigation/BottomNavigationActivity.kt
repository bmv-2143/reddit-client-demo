package com.example.finalattestationreddit.presentation.bottom_navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.ActivityBottomNavigationBinding

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
        navView.setupWithNavController(navController)
    }

    private fun setupActionBarWithNavController() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_subreddits,
                R.id.navigation_favorites,
                R.id.navigation_user_profile
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, BottomNavigationActivity::class.java)
        }
    }
}