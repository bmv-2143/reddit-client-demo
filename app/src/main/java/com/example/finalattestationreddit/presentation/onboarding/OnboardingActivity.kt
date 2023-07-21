package com.example.finalattestationreddit.presentation.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.ActivityOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPagerAndAdapter()
        linkTabLayoutWithViewPager2()
        setSkipButtonListener()
        updateSkipButtonTextDependingOnFragment()
    }

    private fun setupViewPagerAndAdapter() {
        onboardingAdapter = OnboardingAdapter(this)
        binding.viewPager.adapter = onboardingAdapter
    }

    private fun linkTabLayoutWithViewPager2() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }
            .attach()
    }

    private fun setSkipButtonListener() {
        binding.activityOnboardingButtonSkip.setOnClickListener {
            finish()
        }
    }

    private fun updateSkipButtonTextDependingOnFragment() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == OnboardingAdapter.NUMBER_OF_FRAGMENTS - 1)
                    binding.activityOnboardingButtonSkip.text =
                        getString(R.string.activity_onboarding_button_done)
                else
                    binding.activityOnboardingButtonSkip.text =
                        getString(R.string.activity_onboarding_button_skip)
            }
        })
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, OnboardingActivity::class.java)
        }
    }
}
