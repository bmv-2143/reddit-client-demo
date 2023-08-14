package com.example.finalattestationreddit.presentation.bottom_navigation.comments_list


import android.os.Bundle
import android.util.Log
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
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalattestationreddit.BuildConfig
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.databinding.FragmentCommentsListBinding
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.post.PostCommentsAdapter
import com.example.finalattestationreddit.presentation.bottom_navigation.post.PostInfoFragmentDirections
import com.example.finalattestationreddit.presentation.utils.TimeUtils
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommentsListFragment : ViewBindingFragment<FragmentCommentsListBinding>() {

    private val viewModel: CommentsListViewModel by viewModels()
    private val activityViewModel: BottomNavigationViewModel by activityViewModels()

    private val commentsAdapter: PostCommentsAdapter by lazy {
        PostCommentsAdapter(
            timeUtils::formatElapsedTime,
            ::onCommentDownloadButtonClick,
            ::onCommentItemUpVoteClick,
            ::onCommentItemDownVoteClick,
            ::onAuthorClick,
        )
    }
    private val navigationArgs : CommentsListFragmentArgs by navArgs()
    private val launchMode: String by lazy { navigationArgs.launchMode }

    @Inject
    lateinit var timeUtils: TimeUtils

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    private val args: CommentsListFragmentArgs by navArgs()


    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentCommentsListBinding =
        FragmentCommentsListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentPostInfoRecyclerViewComments.adapter = commentsAdapter
        configureLaunchMode(binding)

        if (requireArguments().getInt(ARG_NUMBER_OF_COMMENTS) > 0) {
            observeComments()
            startLoadingComments()
        }

        observerUpdatedComments()
    }

    private fun configureLaunchMode(binding: FragmentCommentsListBinding) {
        when (launchMode) {
            LAUNCH_MODE_EMBEDED_NO_TOOLBAR -> binding.fragmentCommentsListToolbar.visibility =
                View.GONE

            LAUNCH_MODE_SEPARATE_WITH_TOOLBAR -> {
                binding.fragmentCommentsListToolbar.visibility = View.VISIBLE
                initToolbar()
            }

            else -> Log.e(TAG, "Unknown launch mode: $launchMode")
        }
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.fragmentCommentsListToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarTitleSetter.setToolbarTitle(args.postTitle)
    }


    private fun observeComments() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.comments.collectLatest { comments ->
                displayComments(comments)
            }
        }
    }

    private fun startLoadingComments() {
        when (launchMode) {
            LAUNCH_MODE_EMBEDED_NO_TOOLBAR ->
                startLoadingComments(BuildConfig.POST_COMMENTS_PAGE_SIZE_MIN)

            LAUNCH_MODE_SEPARATE_WITH_TOOLBAR ->
                startLoadingComments(BuildConfig.POST_COMMENTS_PAGE_SIZE_MAX)

            else -> Log.e(TAG, "Unknown launch mode: $launchMode")
        }
    }

    private fun startLoadingComments(commentsCountLimit: Int) {
        binding.fragmentCommentsListProgressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            val subredditName = requireArguments().getString(ARG_SUBREDDIT_NAME)
            val postId = requireArguments().getString(ARG_POST_ID)
            if (subredditName != null || postId != null) {
                viewModel.startLoadingPostComments(
                    subredditName!!, postId!!, commentsCountLimit
                ) // TODO: fix me
            }
        }
    }

    private fun displayComments(comments: List<Comment>) {
        commentsAdapter.submitList(comments) {
            binding.fragmentCommentsListProgressBar.visibility = View.GONE
        }
    }

    private fun onCommentDownloadButtonClick(comment: Comment) {

        // STUB: not required by the assignment, no implementation details are given
        Toast.makeText(
            requireContext(),
            getString(R.string.list_item_post_comment_btn_download_msg, comment.name),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onCommentItemUpVoteClick(comment: Comment) = viewModel.upVote(comment)

    private fun onCommentItemDownVoteClick(comment: Comment) = viewModel.downVote(comment)

    private fun onAuthorClick(authorName: String) {
        activityViewModel.setSelectedUser(authorName)
        Toast.makeText(
            requireContext(),
            "AUTHOR CLICKED: $authorName",
            Toast.LENGTH_SHORT
        ).show()
        openUserFragment(authorName)
    }

    private fun openUserFragment(authorName: String) =
        findNavController().navigate(getOpenUserFragmentNavAction(authorName))

    private fun getOpenUserFragmentNavAction(authorName: String): NavDirections {
        return if (launchMode == LAUNCH_MODE_EMBEDED_NO_TOOLBAR) {
            PostInfoFragmentDirections.actionPostInfoFragmentToUserFragment(
                username = authorName
            )
        } else {
            CommentsListFragmentDirections.actionCommentsListFragmentToUserFragment(
                username = authorName
            )
        }
    }

    private fun observerUpdatedComments() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updatedCommentsFlow.filterNotNull().collectLatest { updatedComment ->
                    commentsAdapter.updateComment(updatedComment)
                }
            }
        }
    }

    companion object {

        private const val ARG_LAUNCH_MODE = "launch_mode"
        private const val ARG_SUBREDDIT_NAME = "subreddit_name"
        private const val ARG_POST_ID = "post_id"
        private const val ARG_NUMBER_OF_COMMENTS = "number_of_comments"

        const val LAUNCH_MODE_EMBEDED_NO_TOOLBAR = "embeded_no_toolbar"
        const val LAUNCH_MODE_SEPARATE_WITH_TOOLBAR = "separate_with_toolbar"

        fun newInstance(
            launchMode: String, subredditName: String, postId: String, numberOfComments: Int
        ) = CommentsListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_LAUNCH_MODE, launchMode)
                putString(ARG_SUBREDDIT_NAME, subredditName)
                putString(ARG_POST_ID, postId)
                putInt(ARG_NUMBER_OF_COMMENTS, numberOfComments)
            }
        }
    }
}
