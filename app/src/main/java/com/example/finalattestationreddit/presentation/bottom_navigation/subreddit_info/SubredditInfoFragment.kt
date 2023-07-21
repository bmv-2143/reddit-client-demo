package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.finalattestationreddit.databinding.FragmentSubredditInfoBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListFragmentArgs


class SubredditInfoFragment : ViewBindingFragment<FragmentSubredditInfoBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditInfoBinding =
        FragmentSubredditInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        val args = PostsListFragmentArgs.fromBundle(requireArguments())
        setActionbarTitle(args.subredditData.displayNamePrefixed)

        binding.fragmentSubredditInfoDescription.text = args.subredditData.publicDescription
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentSubredditInfoToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
    }

}