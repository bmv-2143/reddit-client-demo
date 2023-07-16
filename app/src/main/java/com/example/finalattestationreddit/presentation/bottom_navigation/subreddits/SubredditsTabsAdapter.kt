package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.SubredditsListFragment

class SubredditsTabsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUMBER_OF_FRAGMENTS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FRAGMENT_POSITION_NEW_SUBREDDITS -> SubredditsListFragment.newInstance(
                1,
                "New Subreddits"
            )

            else -> SubredditsListFragment.newInstance(1, "Popular Subreddits")
        }
    }

    companion object {
        const val NUMBER_OF_FRAGMENTS = 2
        const val FRAGMENT_POSITION_NEW_SUBREDDITS = 0
    }
}