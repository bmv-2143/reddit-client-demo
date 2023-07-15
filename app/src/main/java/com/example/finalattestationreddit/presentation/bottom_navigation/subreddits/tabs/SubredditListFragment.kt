package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unsplashattestationproject.R
import com.example.finalattestationreddit.presentation.bottom_navigation.subreddits.tabs.placeholder.PlaceholderContent
import com.example.unsplashattestationproject.databinding.FragmentSubredditListBinding
import com.example.unsplashattestationproject.databinding.FragmentSubredditsBinding

/**
 * A fragment representing a list of Items.
 */
class SubredditListFragment : Fragment() {

    private var _binding: FragmentSubredditListBinding? = null
    private val binding get() = _binding!!

    private var columnCount = 1
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            message = it.getString(ARG_MESSAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubredditListBinding.inflate(inflater, container, false)


        binding.message.text = message
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

        binding.fragmentSubredditListButtonOpenItem.setOnClickListener {
            onItemClick("item")
        }

        return binding.root
    }

    private fun onItemClick(item : String) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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