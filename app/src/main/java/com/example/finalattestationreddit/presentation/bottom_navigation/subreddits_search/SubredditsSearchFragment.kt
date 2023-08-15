package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.databinding.FragmentSubredditsSearchBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.SubredditsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubredditsSearchFragment : ViewBindingFragment<FragmentSubredditsSearchBinding>() {

    private val viewModel: SubredditsSearchViewModel by viewModels()
    private val activityViewModel : BottomNavigationViewModel by activityViewModels()

    private val navigationArgs : SubredditsSearchFragmentArgs by navArgs()

    private lateinit var searchMenuProvider: SearchMenuProvider
    private val subredditsAdapter = SubredditsPagingAdapter(
        ::onSubredditItemClick, ::onSubredditSubscribeClick)

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditsSearchBinding {
        return FragmentSubredditsSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiToolbar()
        initSearchMenuProvider(navigationArgs.searchQuery)
        addActionBarMenu()
        binding.fragmentSubredditSearchRecyclerView.adapter = subredditsAdapter
        observerSubscriptionUpdatesFlow()
    }

    private fun intiToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentSubredditsSearchToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun initSearchMenuProvider(initialQuery: String?) {
        searchMenuProvider = SearchMenuProvider(
            initialQuery,
            onSearchQuerySubmit = ::search,
            getString(R.string.fragment_subreddits_search_hint)
        )
    }

    private fun search(searchQuery : String) {
        binding.fragmentSubredditSearchProgressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchSubreddits(searchQuery).collectLatest {
                    binding.fragmentSubredditSearchProgressBar.visibility = View.GONE
                    subredditsAdapter.submitData(it)
                }
            }
        }
    }

    private fun addActionBarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(searchMenuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun onSubredditSubscribeClick(subreddit: SubredditData) =
        activityViewModel.switchSubscription(subreddit)

    private fun onSubredditItemClick(subredditData: SubredditData) {
        activityViewModel.setSelectedSubreddit(subredditData)
        findNavController().navigate(R.id.action_subredditsSearchFragment_to_posts_list_fragment)
    }

    private fun observerSubscriptionUpdatesFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.subscriptionUpdatesFlow.collectLatest { subredditData ->
                    subredditsAdapter.updateItem(subredditData)
                }
            }
        }
    }
}