package com.example.finalattestationreddit.presentation.bottom_navigation.ui.subreddits

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.finalattestationreddit.presentation.bottom_navigation.ui.subreddits.tabs.SubredditListFragment

class SubredditsTabsAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SubredditListFragment.newInstance(1, "AAA")
            else -> SubredditListFragment.newInstance(2, "BBB")
        }
    }
}