package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListType.ALL_POSTS
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListType.SAVED_POSTS
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListType.SUBREDDIT_POSTS
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListType.USER_POSTS
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

    private val postsPagingAdapter = PostsPagingAdapter(::onPostItemClick)

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
        when (val postsType = getPostsTypeArg()) {
            SUBREDDIT_POSTS -> {
                val subredditData = activityViewModel.selectedSubredditFlow.value
                if (subredditData != null) {
                    viewModel.setPostsTarget(subredditData.displayName)
                    observePostsFlow(postsType)
                } else {
                    Log.e(TAG, "Subreddit data is null")
                }
            }

            USER_POSTS -> {
                activityViewModel.selectedUserFlow.value?.let { userName -> // todo: filter blank user names
                    if (userName.isNotBlank()) {
                        viewModel.setPostsTarget(userName)
                        observePostsFlow(postsType)
                    } else {
                        Log.e(TAG, "User name is blank")
                    }
                }
            }

            ALL_POSTS, SAVED_POSTS -> {
                observePostsFlow(postsType)
            }

            null -> Log.e(TAG, "Posts List type is null")
        }
        observeAdapterLoadStateAndUpdateProgressBar()
    }

    @Suppress("DEPRECATION")
    private fun getPostsTypeArg(): PostsListType? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
            arguments?.getSerializable(ARG_POSTS_TYPE) as? PostsListType
        else
            arguments?.getSerializable(ARG_POSTS_TYPE, PostsListType::class.java)

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

    private fun observePostsFlow(postsType: PostsListType) {
        doOnLifecycleStarted {
            viewModel.getPostsFlow(postsType)
                .collectLatest { pagingData ->
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
        const val ARG_POSTS_TYPE = "posts_type"

        fun newUserPostsInstance(
            showToolbar: Boolean,
            postItemClickListener: PostItemClickListener
        ): PostsListFragment { // todo: which list type?
            return PostsListFragment().apply {
                onPostItemClickListener = WeakReference(postItemClickListener)
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_TOOLBAR, showToolbar)
                    putSerializable(ARG_POSTS_TYPE, USER_POSTS)
                }
            }
        }

        fun newAllPostsInstance(postItemClickListener: PostItemClickListener): PostsListFragment { // todo: which list type?
            return PostsListFragment().apply {
                onPostItemClickListener = WeakReference(postItemClickListener)
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_TOOLBAR, false)
                    putSerializable(ARG_POSTS_TYPE, ALL_POSTS)
                }
            }
        }

        fun newSavedPostsInstance(postItemClickListener: PostItemClickListener): PostsListFragment { // todo: which list type?
            return PostsListFragment().apply {
                onPostItemClickListener = WeakReference(postItemClickListener)
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_TOOLBAR, false)
                    putSerializable(ARG_POSTS_TYPE, SAVED_POSTS)
                }
            }
        }
    }
}