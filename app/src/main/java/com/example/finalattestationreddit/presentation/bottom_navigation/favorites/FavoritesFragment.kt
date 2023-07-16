package com.example.finalattestationreddit.presentation.bottom_navigation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textDashboard
        favoritesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        setItemClickListeners() // todo: should be updated

        return binding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}