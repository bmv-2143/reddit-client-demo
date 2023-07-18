package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.FragmentPostsListBinding

class PostsListFragment : ViewBindingFragment<FragmentPostsListBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostsListBinding =
        FragmentPostsListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setButtonClickListener()

        val args = PostsListFragmentArgs.fromBundle(requireArguments())
        setActionbarTitle(args.subredditData.displayNamePrefixed)
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentPostsListToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initToolbarMenu()
    }

    private fun initToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            PostsListMenuProvider(
                onNavigateUpClick = ::navigateUp,
                onSubredditInfoMenuClick = ::navigateToSubredditInfoFragment
            ),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun navigateUp() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun navigateToSubredditInfoFragment() {
        findNavController().navigate(R.id.action_posts_list_fragment_to_subredditInfoFragment)
    }


    private fun setButtonClickListener() {
//        binding.fragmentPostsListButtonOpenPost.setOnClickListener {
//            findNavController().navigate(R.id.action_postsListFragment_to_postFragment)
//        }
    }

    private fun hideActionbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

}

