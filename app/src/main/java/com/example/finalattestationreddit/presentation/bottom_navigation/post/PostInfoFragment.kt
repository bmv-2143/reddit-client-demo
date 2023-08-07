package com.example.finalattestationreddit.presentation.bottom_navigation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.databinding.FragmentPostInfoBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.comments_list.CommentsListFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostImageLoader
import com.example.finalattestationreddit.presentation.utils.ShareUtils
import com.example.finalattestationreddit.presentation.utils.TimeUtils
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import com.example.finalattestationreddit.presentation.widgets.ScoreVotingViewGroup.VoteState.DOWN_VOTED
import com.example.finalattestationreddit.presentation.widgets.ScoreVotingViewGroup.VoteState.INITIAL
import com.example.finalattestationreddit.presentation.widgets.ScoreVotingViewGroup.VoteState.UP_VOTED
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostInfoFragment : ViewBindingFragment<FragmentPostInfoBinding>() {

    private val activityViewModel: BottomNavigationViewModel by activityViewModels()
    private val viewModel: PostInfoViewModel by viewModels()

    @Inject
    lateinit var timeUtils: TimeUtils

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    @Inject
    lateinit var shareUtils: ShareUtils

    private lateinit var selectedAndUpdatedPostFlow: Flow<Post?>
    private var latestSelectedOrUpdatedPost: Post? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostInfoBinding =
        FragmentPostInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedAndUpdatedPostFlow =
            merge(activityViewModel.selectedPostFlow, viewModel.updatedPostFlow)

        initToolbar()
        collectSelectedPost()
        setShareButtonListener()
        setVoteControlsListeners()
        setShowAllCommentsButtonListener()
        setPostAuthorClickListener()
        addCommentsFragment()
    }

    // todo: DRY, see other toolbar fragments
    private fun initToolbar() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.fragmentPostsListToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun collectSelectedPost() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectedAndUpdatedPostFlow.filterNotNull().collectLatest { post ->
                    latestSelectedOrUpdatedPost = post
                    updateUi(post)
                }
            }
        }
    }


    private fun updateUi(post: Post) {
        toolbarTitleSetter.setToolbarTitle(post.title)
        updatePostTexts(post)
        updatePostVoteControls(post)
        loadPostImage(post)
        updateShowAllCommentsButtonVisibility(post)
    }

    private fun updatePostTexts(post: Post) {
        binding.fragmentPostInfoBodyText.text = post.selfText
        binding.fragmentPostInfoAuthor.text = post.author
        binding.fragmentPostInfoPublicationTime.text = timeUtils.formatElapsedTime(post.createdUtc)
        binding.fragmentPostInfoNumberOfComments.text =
            getString(R.string.fragment_post_info_number_of_comments, post.numComments)
    }

    private fun updatePostVoteControls(post: Post) {
        binding.fragmentPostInfoScoreVoting.setScore(post.score)

        when (post.likedByUser) {
            true -> binding.fragmentPostInfoScoreVoting.setVoteState(UP_VOTED)

            false -> binding.fragmentPostInfoScoreVoting.setVoteState(DOWN_VOTED)

            null -> binding.fragmentPostInfoScoreVoting.setVoteState(INITIAL)
        }
    }

    private fun loadPostImage(post: Post) {
        PostImageLoader(
            requireContext(), binding.fragmentPostInfoImage,
            onComplete = ::hideProgressBar,
            onLoadFailed = ::hideProgressBar
        ).loadImage(post)
    }

    private fun hideProgressBar() {
        binding.fragmentPostInfoProgressBar.visibility = View.GONE
    }

    private fun setShareButtonListener() {
        binding.fragmentPostInfoShare.setOnClickListener {
            val post = latestSelectedOrUpdatedPost
            val shareLink = viewModel.getShareLink(post)
            shareUtils.shareUrl(
                shareLink,
                getString(
                    R.string.fragment_post_info_share_chooser_title
                )
            )
        }
    }

    private fun setVoteControlsListeners() {
        binding.fragmentPostInfoScoreVoting.onDownVoteClickListener = {
            latestSelectedOrUpdatedPost?.let {
                viewModel.downVote(it)
            }
        }

        binding.fragmentPostInfoScoreVoting.onUpVoteClickListener = {
            latestSelectedOrUpdatedPost?.let {
                viewModel.upVote(it)
            }
        }
    }

    private fun setShowAllCommentsButtonListener() {
        binding.fragmentPostInfoButtonShowAllComments.setOnClickListener {
            openCommentsListFragment()
        }
    }

    private fun setPostAuthorClickListener() {
        viewLifecycleOwner.lifecycleScope.launch {              // todo: don't collect in click listener
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectedAndUpdatedPostFlow.filterNotNull().collectLatest { post ->
                    binding.fragmentPostInfoAuthor.setOnClickListener {
                        activityViewModel.setSelectedUser(post.author)
                        Toast.makeText(
                            requireContext(),
                            "AUTHOR CLICKED: ${post.author}",
                            Toast.LENGTH_SHORT
                        ).show()
                        openUserFragment(post.author)
                    }
                }
            }
        }
    }

    private fun openUserFragment(userName: String) {
        val action = PostInfoFragmentDirections.actionPostInfoFragmentToUserFragment(
            username = userName
        )
        findNavController().navigate(action)
    }

    private fun openCommentsListFragment() {
        doWithSubredditAndPost { subreddit, post ->
            val action = PostInfoFragmentDirections.actionPostInfoFragmentToCommentsListFragment(
                launchMode = CommentsListFragment.LAUNCH_MODE_SEPARATE_WITH_TOOLBAR,
                subredditName = subreddit.displayName,
                postId = post.getPostId(),
                postTitle = post.title,
                numberOfComments = post.numComments
            )
            findNavController().navigate(action)
        }
    }

    private fun doWithSubredditAndPost(action: (subreddit: SubredditData, post: Post) -> Unit) {
        activityViewModel.selectedSubredditFlow.value?.let { subreddit ->
            activityViewModel.selectedPostFlow.value?.let { post ->
                action(subreddit, post)
            }
        }
    }

    private fun addCommentsFragment() {
        doWithSubredditAndPost(::addCommentsFragment)
    }

    private fun addCommentsFragment(subreddit: SubredditData, post: Post) {
        val fragment = CommentsListFragment
            .newInstance(
                CommentsListFragment.LAUNCH_MODE_EMBEDED_NO_TOOLBAR,
                subreddit.displayName,
                post.getPostId(),
                post.numComments
            )
        val transaction = childFragmentManager.beginTransaction() // todo: which fragment manager?
        transaction.add(R.id.fragment_post_info_comments_fragment_container, fragment)
        transaction.commit()
    }

    private fun updateShowAllCommentsButtonVisibility(post: Post) {
        binding.fragmentPostInfoButtonShowAllComments.visibility =
            if (viewModel.shouldDisplayShowAllCommentsButton(post))
                View.VISIBLE
            else
                View.GONE
    }
}