package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.databinding.FragmentSubredditsSearchBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.SubredditsPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubredditsSearchFragment : ViewBindingFragment<FragmentSubredditsSearchBinding>() {

    private val viewModel: SubredditsSearchViewModel by viewModels()
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
        initSearchMenuProvider(arguments?.getString("search_query"))
        addActionBarMenu()
        binding.fragmentSubredditSearchRecyclerView.adapter = subredditsAdapter
    }

    private fun intiToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentSubredditsSearchToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun initSearchMenuProvider(intialQuery: String?) {
        searchMenuProvider = SearchMenuProvider(
            intialQuery,
            onSearchQuerySubmit = { query ->
                Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
                search(query)

            },
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

    private fun onSubredditSubscribeClick(subredditData: SubredditData) {
        TODO("Not yet implemented")
    }

    private fun onSubredditItemClick(subredditData: SubredditData) {
    }
}