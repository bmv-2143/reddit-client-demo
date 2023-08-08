package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.databinding.FragmentPostsListBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class PostsListFragment : ViewBindingFragment<FragmentPostsListBinding>() {

    private val viewModel: PostsListViewModel by viewModels()
    private val activityViewModel: BottomNavigationViewModel by activityViewModels()
    internal var onPostItemClickListener: WeakReference<PostItemClickListener>? = null

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    private val postsPagingAdapter = PostsPagingAdapter(
        ::onPostItemClick,
        {
            // todo: implement or delete
        }
    )


    private fun onPostItemClick(post: Post) {
        // todo: crashes when clicked on an item in the nested posts list fragment view
        if (getShowToolbarArg()) {
            activityViewModel.setSelectedPost(post)
            findNavController().navigate(R.id.action_postsListFragment_to_postFragment)
        } else {
            onPostItemClickListener?.get()?.onPostItemClick(post)
        }
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

        loadPosts()

    }

    private fun loadPosts() {
        if (getShowToolbarArg()) {
            activityViewModel.selectedSubredditFlow.value?.let { subredditData ->
                observeSubredditPostsFlow(subredditData.displayName)
            }
        }
        else {
            activityViewModel.selectedUserFlow.value?.let { userName -> // todo: filter blank user names
                if (userName.isNotBlank())
                    observeUserPostsFlow(userName)
            }
        }

        observeAdapterLoadStateAndUpdateProgressBar()
    }

    private fun getShowToolbarArg(): Boolean =
        arguments?.getBoolean(ARG_SHOW_TOOLBAR) ?: true

    private fun initToolbar() {
        if (!getShowToolbarArg())
            return

        binding.fragmentPostsListToolbar.visibility = View.VISIBLE

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentPostsListToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initToolbarMenu()

        activityViewModel.selectedSubredditFlow.value?.let { subredditData ->
            toolbarTitleSetter.setToolbarTitle(subredditData.displayName)
        }
    }

    private fun initToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            PostsListMenuProvider(
                onSubredditInfoMenuClick = ::navigateToSubredditInfoFragment
            ),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun navigateToSubredditInfoFragment() {
        val navigateToSubredditInfoAction = PostsListFragmentDirections
            .actionPostsListFragmentToSubredditInfoFragment()
        findNavController().navigate(navigateToSubredditInfoAction)
    }

    private fun setupRecyclerView() {
        binding.fragmentPostsListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.fragmentPostsListRecyclerView.adapter = postsPagingAdapter
    }

    private fun observeSubredditPostsFlow(subredditDisplayName: String) {
        doOnLifecycleStarted {
            viewModel.getPostsFlow(subredditDisplayName).collectLatest { pagingData ->
                postsPagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeUserPostsFlow(username: String) {
        doOnLifecycleStarted {
            viewModel.getUserPostsFlow(username).collectLatest { pagingData ->
                postsPagingAdapter.submitData(pagingData)
            }
        }
    }


    private fun doOnLifecycleStarted(action: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                action()
            }
        }
    }

    private fun observeAdapterLoadStateAndUpdateProgressBar() {
        postsPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                binding.fragmentPostsListProgressBar.visibility = View.VISIBLE
            } else {
                binding.fragmentPostsListProgressBar.visibility = View.GONE
            }
        }
    }

    companion object {

        const val ARG_SHOW_TOOLBAR = "show_toolbar"

        fun createNoToolbarInstance(postItemClickListener: PostItemClickListener): PostsListFragment {
            return PostsListFragment().apply {
                onPostItemClickListener = WeakReference(postItemClickListener)
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_TOOLBAR, false)
                }
            }
        }

    }
}