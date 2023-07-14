package com.example.finalattestationreddit.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = NUMBER_OF_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FRAGMENT_POSITION_WELCOME -> OnboardingWelcomeFragment.newInstance()
            FRAGMENT_POSITION_SUBHUMBLR -> OnboardingSubhumblrFragment.newInstance()
            else -> OnboardingShareAndSaveFragment.newInstance()
        }
    }

    companion object {
        const val NUMBER_OF_FRAGMENTS = 3
        const val FRAGMENT_POSITION_WELCOME = 0
        const val FRAGMENT_POSITION_SUBHUMBLR = 1
    }
}