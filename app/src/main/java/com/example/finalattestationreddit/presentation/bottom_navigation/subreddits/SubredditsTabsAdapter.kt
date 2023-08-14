package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.finalattestationreddit.data.network.SubredditListType.VAL_SUBREDDITS_LIST_TYPE_NEW
import com.example.finalattestationreddit.data.network.SubredditListType.VAL_SUBREDDITS_LIST_TYPE_POPULAR
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.SubredditItemClickListener
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.SubredditsListFragment

class SubredditsTabsAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val subredditItemClickListener: SubredditItemClickListener
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUMBER_OF_FRAGMENTS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            FRAGMENT_POSITION_NEW_SUBREDDITS ->
                SubredditsListFragment.newInstance(
                    VAL_SUBREDDITS_LIST_TYPE_NEW,
                    subredditItemClickListener
                )

            else ->
                SubredditsListFragment.newInstance(
                    VAL_SUBREDDITS_LIST_TYPE_POPULAR,
                    subredditItemClickListener
                )
        }
    }

    companion object {
        const val NUMBER_OF_FRAGMENTS = 2
        const val FRAGMENT_POSITION_NEW_SUBREDDITS = 0
    }
}