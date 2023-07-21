package com.example.finalattestationreddit.presentation.bottom_navigation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.FragmentFavoritesBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment

class FavoritesFragment : ViewBindingFragment<FragmentFavoritesBinding>() {

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textDashboard
        favoritesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        setItemClickListeners() // todo: should be updated
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

}