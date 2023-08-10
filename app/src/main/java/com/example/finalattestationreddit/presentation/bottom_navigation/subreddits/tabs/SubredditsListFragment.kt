package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.data.network.SubredditListType.ARG_SUBREDDITS_LIST_TYPE
import com.example.finalattestationreddit.databinding.FragmentSubredditsListBinding
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SubredditsListFragment : ViewBindingFragment<FragmentSubredditsListBinding>() {

    private val viewModel: SubredditsListViewModel by viewModels()
    private val activityViewModel: BottomNavigationViewModel by activityViewModels()
    internal var onSubredditItemClickListener: SubredditItemClickListener? = null

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
                activityViewModel.subscriptionUpdatesFlow.collectLatest { subredditData ->
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

    // todo: refactor this method
    private fun onSubredditItemClick(subredditData: SubredditData) {
        val subredditClickListener = onSubredditItemClickListener
        if (subredditClickListener == null) {
            Log.e(TAG, "onSubredditItemClick: onSubredditItemClickListener is null")
            return
        }
        subredditClickListener.onSubredditItemClick(subredditData)
    }

    private fun onSubredditSubscribeButtonClick(subreddit: SubredditData) {
        Toast.makeText(
            requireContext(),
            "Subreddit ${subreddit.displayName} subscribed",
            Toast.LENGTH_SHORT
        ).show()

        activityViewModel.switchSubscription(subreddit)
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
        fun newInstance(
            subredditsListType: String,
            subredditItemClickListener: SubredditItemClickListener
        ) =
            SubredditsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SUBREDDITS_LIST_TYPE, subredditsListType)
                }
                onSubredditItemClickListener = subredditItemClickListener
            }
    }
}