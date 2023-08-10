package com.example.finalattestationreddit.presentation.bottom_navigation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.network.SubredditListType
import com.example.finalattestationreddit.databinding.FragmentFavoritesBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.favorites.FavoritesFragmentTogglesState.PostsAll
import com.example.finalattestationreddit.presentation.bottom_navigation.favorites.FavoritesFragmentTogglesState.PostsSaved
import com.example.finalattestationreddit.presentation.bottom_navigation.favorites.FavoritesFragmentTogglesState.SubredditsAll
import com.example.finalattestationreddit.presentation.bottom_navigation.favorites.FavoritesFragmentTogglesState.SubredditsSaved
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.SubredditsListFragment
import kotlinx.coroutines.launch

class FavoritesFragment : ViewBindingFragment<FragmentFavoritesBinding>() {

    private val viewModel: FavoritesViewModel by viewModels()


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentFavoritesToggleGroupSubredditsPosts.check(R.id.fragment_favorites_button_subreddits)
        binding.fragmentFavoritesToggleGroupAllSaved.check(R.id.fragment_favorites_button_all)

        addAllSubredditsFragment()
        setSubredditsPostsToggleGroupListener()
        setAllSavedToggleGroupListener()

        observeTogglesStateFlow()
    }

    private fun setSubredditsPostsToggleGroupListener() {
        binding.fragmentFavoritesToggleGroupSubredditsPosts
            .addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked)
                    when (checkedId) {
                        R.id.fragment_favorites_button_subreddits ->
                            viewModel.onSubredditsToggleSelected()

                        R.id.fragment_favorites_button_posts -> viewModel.onPostsToggleSelected()
                    }
            }
    }

    private fun setAllSavedToggleGroupListener() {
        binding.fragmentFavoritesToggleGroupAllSaved
            .addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked)
                    when (checkedId) {
                        R.id.fragment_favorites_button_saved -> viewModel.onSavedToggleSelected()

                        R.id.fragment_favorites_button_all -> viewModel.onAllToggleSelected()
                    }
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

    private fun observeTogglesStateFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.togglesStateFlow.collect { togglesState ->
                    handleTogglesStateChange(togglesState)
                }
            }
        }
    }

    private fun handleTogglesStateChange(togglesState: FavoritesFragmentTogglesState) {
        when (togglesState) {
            is SubredditsAll -> addAllSubredditsFragment()

            is SubredditsSaved -> addSubscribedSubredditsFragment()

            is PostsAll -> addAllPostsFragment()

            is PostsSaved -> addMySavedPostsFragment()
        }
    }


    private fun addAllSubredditsFragment() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragment_favorites_fragment_container_list,
            makeAllSubredditsListFragment()
        )
        transaction.commit()
    }

    private fun makeAllSubredditsListFragment(): SubredditsListFragment {
        return SubredditsListFragment.newInstance(
            SubredditListType.VAL_SUBREDDITS_LIST_TYPE_UNSPECIFIED
        )
    }

    private fun addSubscribedSubredditsFragment() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(
            R.id.fragment_favorites_fragment_container_list,
            makeSubscribedSubredditsListFragment()
        )
        transaction.commit()
    }

    private fun makeSubscribedSubredditsListFragment(): SubredditsListFragment {
        return SubredditsListFragment.newInstance(
            SubredditListType.VAL_SUBREDDITS_LIST_TYPE_SUBSCRIBED
        )
    }

    private fun addAllPostsFragment() {
        val transaction = childFragmentManager.beginTransaction()

        transaction.replace(
            R.id.fragment_favorites_fragment_container_list,
            makeAllPostsFragment()
        )
        transaction.commit()
    }

    private fun makeAllPostsFragment(): PostsListFragment {
        return PostsListFragment.newAllPostsInstance(
            {
                // todo
            })

    }

    private fun addMySavedPostsFragment() {
        val transaction = childFragmentManager.beginTransaction()

        transaction.replace(
            R.id.fragment_favorites_fragment_container_list,
            makeMySavedPostsFragment()
        )
        transaction.commit()
    }

    private fun makeMySavedPostsFragment(): PostsListFragment {
        return PostsListFragment.newSavedPostsInstance(
            {
                // todo
            })

    }
}