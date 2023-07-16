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
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.FragmentSubredditsListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SubredditsListFragment : ViewBindingFragment<FragmentSubredditsListBinding>() {

    private val viewModel: SubredditsListViewModel by viewModels()

    private val subredditsAdapter = SubredditListItemRecyclerViewAdapter(PlaceholderContent.ITEMS)

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditsListBinding =
        FragmentSubredditsListBinding.inflate(inflater, container, false)

    private var columnCount = 1
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            message = it.getString(ARG_MESSAGE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.message.text = message

        setupRecyclerView()

        binding.fragmentSubredditListButtonOpenItem.setOnClickListener {
            onItemClick("item")
        }

        viewModel.getNewSubreddits()
        observerSubredditsFlow()
    }

    private fun observerSubredditsFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.subredditsFlow.collectLatest { pagingData ->
                    subredditsAdapter.setData(pagingData.map { subredditData ->
                        PlaceholderContent.PlaceholderItem(
                            "id",
                            subredditData.title,
                            subredditData.subscribers.toString()
                        )
                    })


                }
            }
        }
    }

    private fun setupRecyclerView() {
        // Set the adapter
        //        if (view is RecyclerView) {
        //            with(view) {
        //                layoutManager = when {
        //                    columnCount <= 1 -> LinearLayoutManager(context)
        //                    else -> GridLayoutManager(context, columnCount)
        //                }
        //                adapter = SubredditListItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
        //            }
        //        }

        binding.fragmentSubredditsListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.fragmentSubredditsListRecyclerView.adapter = subredditsAdapter
    }

    private fun onItemClick(item: String) {
        if (findNavController().currentDestination?.id == R.id.navigation_subreddits) {
            findNavController().navigate(R.id.action_navigation_subreddits_to_postsListFragment)
        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_MESSAGE = "message"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int, message: String) =
            SubredditsListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_MESSAGE, message)
                }
            }
    }
}