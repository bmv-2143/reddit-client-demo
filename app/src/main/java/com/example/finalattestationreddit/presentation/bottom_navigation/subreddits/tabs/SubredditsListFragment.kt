package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.network.SubredditListType.ARG_SUBREDDITS_LIST_TYPE
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.SubredditsFragmentDirections
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.FragmentSubredditsListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SubredditsListFragment : ViewBindingFragment<FragmentSubredditsListBinding>() {

    private val viewModel: SubredditsListViewModel by viewModels()
    private val subredditsPagingAdapter = SubredditsPagingAdapter(
        ::onSubredditItemClick, ::onSubredditSubscribeButtonClick
    )

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditsListBinding =
        FragmentSubredditsListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observerSubredditsFlow()
        observeLoadStateAndUpdateProgressBar()
        observerSubscriptionUpdatesFlow()
    }

    private fun observerSubredditsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subredditsPagedFlow?.collectLatest { pagingData ->
                    subredditsPagingAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observerSubscriptionUpdatesFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subscriptionUpdatesFlow.collectLatest { subredditData ->
                    subredditsPagingAdapter.updateItem(subredditData)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.fragmentSubredditsListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.fragmentSubredditsListRecyclerView.adapter = subredditsPagingAdapter
    }

    private fun onSubredditItemClick(subredditData: SubredditData) {
        if (findNavController().currentDestination?.id == R.id.navigation_subreddits) {
            val action = SubredditsFragmentDirections
                .actionNavigationSubredditsToPostsListFragment(subredditData)
            findNavController().navigate(action)

        }
    }

    private fun onSubredditSubscribeButtonClick(subreddit: SubredditData) {
        Toast.makeText(
            requireContext(),
            "Subreddit ${subreddit.displayName} subscribed",
            Toast.LENGTH_SHORT
        ).show()

        viewModel.switchSubscription(subreddit)
    }

    private fun observeLoadStateAndUpdateProgressBar() {
        subredditsPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                binding.fragmentSubredditsListProgressBar.visibility = View.VISIBLE
            } else {
                binding.fragmentSubredditsListProgressBar.visibility = View.GONE
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(subredditsListType: String) =
            SubredditsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SUBREDDITS_LIST_TYPE, subredditsListType)
                }
            }
    }
}