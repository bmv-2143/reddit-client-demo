package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.FragmentPostsListBinding
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsListFragment : ViewBindingFragment<FragmentPostsListBinding>() {

    private val viewModel: PostsListViewModel by viewModels()

    private val postsPagingAdapter = PostsPagingAdapter(
        ::onPostItemClick,
        {
            // todo: implement or delete
        }
    )

    private fun onPostItemClick(post: Post) {
        findNavController().navigate(R.id.action_postsListFragment_to_postFragment)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostsListBinding =
        FragmentPostsListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setupRecyclerView()

        // todo: its better pass it to a view model? or use state handle from the view model?
        val args = PostsListFragmentArgs.fromBundle(requireArguments())
        setActionbarTitle(args.subredditData.displayNamePrefixed)
        observePostsFlow(args.subredditData.displayName)
        observeLoadStateAndUpdateProgressBar()
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentPostsListToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initToolbarMenu()
    }

    private fun initToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            PostsListMenuProvider(
                onNavigateUpClick = ::navigateUp,
                onSubredditInfoMenuClick = ::navigateToSubredditInfoFragment
            ),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun navigateUp() {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun navigateToSubredditInfoFragment() {
        findNavController().navigate(R.id.action_posts_list_fragment_to_subredditInfoFragment)
    }

    private fun setupRecyclerView() {
        binding.fragmentPostsListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.fragmentPostsListRecyclerView.adapter = postsPagingAdapter
    }

    private fun observePostsFlow(subredditDisplayName : String) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPostsFlow(subredditDisplayName).collectLatest { pagingData ->
                    postsPagingAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeLoadStateAndUpdateProgressBar() {
        postsPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                binding.fragmentPostsListProgressBar.visibility = View.VISIBLE
            } else {
                binding.fragmentPostsListProgressBar.visibility = View.GONE
            }
        }
    }
}