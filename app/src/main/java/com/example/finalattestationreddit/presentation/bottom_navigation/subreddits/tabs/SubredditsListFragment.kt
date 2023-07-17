package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val subredditsPagingAdapter = SubredditsPagingAdapter(::onItemClick)

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditsListBinding =
        FragmentSubredditsListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observerSubredditsFlow()
    }

    private fun observerSubredditsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subredditsPagedFlow.collectLatest { pagingData ->
                    subredditsPagingAdapter.submitData(pagingData)
                }
            }
        }
    }


    private fun setupRecyclerView() {
        binding.fragmentSubredditsListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.fragmentSubredditsListRecyclerView.adapter = subredditsPagingAdapter
    }

    private fun onItemClick(subredditDisplayName: String) {
        if (findNavController().currentDestination?.id == R.id.navigation_subreddits) {
//            val bundle = bundleOf("subredditDisplayName" to subredditDisplayName)
//            findNavController().navigate(R.id.action_navigation_subreddits_to_postsListFragment, bundle)

            val action = SubredditsFragmentDirections
                .actionNavigationSubredditsToPostsListFragment(subredditDisplayName)
            findNavController().navigate(action)

        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, message: String) =
            SubredditsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}