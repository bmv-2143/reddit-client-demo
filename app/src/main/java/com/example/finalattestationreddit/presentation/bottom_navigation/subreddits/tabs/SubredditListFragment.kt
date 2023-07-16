package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.FragmentSubredditListBinding

/**
 * A fragment representing a list of Items.
 */
class SubredditListFragment : ViewBindingFragment<FragmentSubredditListBinding>() {


    private val subredditsAdapter = SubredditListItemRecyclerViewAdapter(PlaceholderContent.ITEMS)

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditListBinding =
        FragmentSubredditListBinding.inflate(inflater, container, false)

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

    private fun onItemClick(item : String) {
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
            SubredditListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_MESSAGE, message)
                }
            }
    }
}