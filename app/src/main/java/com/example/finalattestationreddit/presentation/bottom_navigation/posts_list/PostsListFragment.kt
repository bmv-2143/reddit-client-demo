package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

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
import androidx.navigation.fragment.navArgs
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
    private val navigationArgs: PostsListFragmentArgs by navArgs()
    internal var onPostItemClickListener: WeakReference<PostItemClickListener>? = null

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    private val postsPagingAdapter = PostsPagingAdapter(::onPostItemClick)

    private fun onPostItemClick(post: Post) {
        if (navigationArgs.showToolbar) {
            activityViewModel.setSelectedPost(post)
            findNavController().navigate(R.id.action_postsListFragment_to_postFragment)
        } else {
            onPostItemClickListener?.get()?.onPostItemClick(post)
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentPostsListBinding = FragmentPostsListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setupRecyclerView()
        observePosts()
    }

    private fun observePosts() {
        when (val postsType = navigationArgs.postsType) {
            SUBREDDIT_POSTS -> loadSubredditPosts(postsType)

            USER_POSTS -> loadUserPosts(postsType)

            ALL_POSTS, SAVED_POSTS -> observePostsFlow(postsType)
        }
        observeAdapterLoadStateAndUpdateProgressBar()
    }

    private fun loadSubredditPosts(postsType: PostsListType) {
        val subredditData = activityViewModel.selectedSubredditFlow.value
        if (subredditData != null) {
            viewModel.setPostsTarget(subredditData.displayName)
            observePostsFlow(postsType)
        } else {
            Log.e(TAG, "Subreddit data is null")
        }
    }

    private fun loadUserPosts(postsType: PostsListType) {
        activityViewModel.selectedUserFlow.value?.let { userName -> // todo: filter blank user names
            if (userName.isNotBlank()) {
                viewModel.setPostsTarget(userName)
                observePostsFlow(postsType)
            } else {
                Log.e(TAG, "User name is blank")
            }
        }
    }

    private fun initToolbar() {
        if (!navigationArgs.showToolbar) return

        binding.fragmentPostsListToolbar.visibility = View.VISIBLE
        setupSupportActionBar()
        initToolbarMenu()
        setSubredditToolbarTitle()
    }

    private fun setupSupportActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentPostsListToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            PostsListMenuProvider(onSubredditInfoMenuClick = ::navigateToSubredditInfoFragment),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun setSubredditToolbarTitle() {
        activityViewModel.selectedSubredditFlow.value?.let { subredditData ->
            toolbarTitleSetter.setToolbarTitle(subredditData.displayName)
        }
    }

    private fun navigateToSubredditInfoFragment() {
        val navigateToSubredditInfoAction =
            PostsListFragmentDirections.actionPostsListFragmentToSubredditInfoFragment()
        findNavController().navigate(navigateToSubredditInfoAction)
    }

    private fun setupRecyclerView() {
        binding.fragmentPostsListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.fragmentPostsListRecyclerView.adapter = postsPagingAdapter
    }

    private fun observePostsFlow(postsType: PostsListType) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPostsFlow(postsType).collectLatest { pagingData ->
                    postsPagingAdapter.submitData(pagingData)
                }
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
            showToolbar: Boolean, postItemClickListener: PostItemClickListener
        ): PostsListFragment = newInstance(showToolbar, postItemClickListener, USER_POSTS)

        fun newAllPostsInstance(postItemClickListener: PostItemClickListener): PostsListFragment =
            newInstance(false, postItemClickListener, ALL_POSTS)

        fun newSavedPostsInstance(postItemClickListener: PostItemClickListener): PostsListFragment =
            newInstance(false, postItemClickListener, SAVED_POSTS)

        private fun newInstance(
            showToolbar: Boolean,
            postItemClickListener: PostItemClickListener,
            postsType: PostsListType
        ): PostsListFragment {
            return PostsListFragment().apply {
                onPostItemClickListener = WeakReference(postItemClickListener)
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_TOOLBAR, showToolbar)
                    putSerializable(ARG_POSTS_TYPE, postsType)
                }
            }
        }
    }
}