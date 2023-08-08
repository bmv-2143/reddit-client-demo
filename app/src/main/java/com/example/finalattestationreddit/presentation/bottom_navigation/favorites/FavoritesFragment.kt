package com.example.finalattestationreddit.presentation.bottom_navigation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.network.SubredditListType
import com.example.finalattestationreddit.databinding.FragmentFavoritesBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.SubredditsListFragment

class FavoritesFragment : ViewBindingFragment<FragmentFavoritesBinding>() {

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentFavoritesToggleGroupSubredditsPosts.check(R.id.fragment_favorites_button_subreddits)
        binding.fragmentFavoritesToggleGroupAllSaved.check(R.id.fragment_favorites_button_all)

        setItemClickListeners() // todo: should be updated
        addAllSubredditsFragment()
    }


    private fun setItemClickListeners() {
        binding.fragmentFavoritesButtonOpenSubreddit.setOnClickListener {
            onSubredditItemClick("Subreddit -> PostsList")
        }

        binding.fragmentFavoritesButtonOpenPost.setOnClickListener {
            onPostItemClick("Post -> Post Info")
        }
    }

    private fun onSubredditItemClick(subreddit: String) {
        // todo: do I need this check?
        if (findNavController().currentDestination?.id == R.id.navigation_favorites) {
            findNavController().navigate(R.id.action_navigation_favorites_to_postsListFragment)
        }
    }

    private fun onPostItemClick(subreddit: String) {
        // todo: do I need this check?
        if (findNavController().currentDestination?.id == R.id.navigation_favorites) {
            findNavController().navigate(R.id.action_navigation_favorites_to_postInfoFragment)
        }
    }


    private fun addAllSubredditsFragment() {
//        val fragment = makeAllSubredditsListFragment()
        val fragment = makeSubscribedSubredditsListFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_favorites_fragment_container_list, fragment)
        transaction.commit()
    }

    private fun makeAllSubredditsListFragment(): SubredditsListFragment {
        return SubredditsListFragment.newInstance(
            SubredditListType.VAL_SUBREDDITS_LIST_TYPE_UNSPECIFIED
        )
    }

    private fun makeSubscribedSubredditsListFragment(): SubredditsListFragment {
        return SubredditsListFragment.newInstance(
            SubredditListType.VAL_SUBREDDITS_LIST_TYPE_SUBSCRIBED
        )
    }
}