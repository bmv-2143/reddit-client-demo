package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.FragmentSubredditInfoBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListFragmentArgs
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListMenuProvider
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SubredditInfoFragment : ViewBindingFragment<FragmentSubredditInfoBinding>() {

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditInfoBinding =
        FragmentSubredditInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        val args = PostsListFragmentArgs.fromBundle(requireArguments())
        toolbarTitleSetter.setToolbarTitle(args.subredditData.displayNamePrefixed)

        binding.fragmentSubredditInfoDescription.text = args.subredditData.publicDescription
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentSubredditInfoToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}