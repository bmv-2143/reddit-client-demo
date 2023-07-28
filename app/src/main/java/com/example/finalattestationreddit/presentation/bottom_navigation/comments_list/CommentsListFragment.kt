package com.example.finalattestationreddit.presentation.bottom_navigation.comments_list


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.comment.Comment
import com.example.finalattestationreddit.databinding.FragmentCommentsListBinding
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.post.PostCommentsAdapter
import com.example.finalattestationreddit.presentation.bottom_navigation.post.PostInfoViewModel
import com.example.finalattestationreddit.presentation.utils.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CommentsListFragment : ViewBindingFragment<FragmentCommentsListBinding>() {

    private val viewModel: PostInfoViewModel by viewModels({ requireParentFragment() })

    // TODO: move / or handle in view model?
//    private var subredditName: String? = null
//    private var postId: String? = null

    private val commentsAdapter: PostCommentsAdapter by lazy {
        PostCommentsAdapter(
            timeUtils::formatElapsedTime,
            ::onCommentDownloadButtonClick,
            ::onCommentSaveButtonClick
        )
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCommentsListBinding =
        FragmentCommentsListBinding.inflate(inflater, container, false)

    @Inject
    lateinit var timeUtils: TimeUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCommentsListBinding.inflate(inflater, container, false)
        binding.fragmentPostInfoRecyclerViewComments.adapter = commentsAdapter
        configureLaunchMode(binding)
        return binding.root
    }

    private fun configureLaunchMode(binding: FragmentCommentsListBinding) {
        when (val launchMode = requireArguments().getString(ARG_LAUNCH_MODE)) {
            LAUNCH_MODE_EMBEDED_NO_TOOLBAR ->
                binding.fragmentCommentsListToolbar.visibility = View.GONE

            LAUNCH_MODE_SEPARATE_WITH_TOOLBAR ->
                binding.fragmentCommentsListToolbar.visibility = View.VISIBLE

            else -> Log.e(TAG, "Unknown launch mode: $launchMode")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeComments()
        startLoadingComments()
    }

    private fun startLoadingComments() {
        viewLifecycleOwner.lifecycleScope.launch {

            val subredditName = requireArguments().getString(ARG_SUBREDDIT_NAME)
            val postId = requireArguments().getString(ARG_POST_ID)
            if (subredditName != null || postId != null) {
                viewModel.startLoadingPostComments(subredditName!!, postId!!) // TODO: fix me
            }
        }
    }

    private fun observeComments() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.comments.collectLatest { comments ->
                displayComments(comments.filter { comment -> comment.body != null })
            }
        }
    }

    private fun displayComments(comments: List<Comment>) {
        commentsAdapter.submitList(comments)
    }

    private fun onCommentDownloadButtonClick(comment: Comment) {
        // STUB: not required by the assignment, no implementation details were given

        Toast.makeText(
            requireContext(),
            getString(R.string.list_item_post_comment_btn_download_msg, comment.name),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onCommentSaveButtonClick(comment: Comment) {
        // STUB: not required by the assignment, no implementation details were given

        Toast.makeText(
            requireContext(),
            getString(R.string.list_item_post_comment_btn_save_msg, comment.name),
            Toast.LENGTH_SHORT
        ).show()
    }


    companion object {

        private const val ARG_LAUNCH_MODE = "launch_mode"
        private const val ARG_SUBREDDIT_NAME = "subreddit_name"
        private const val ARG_POST_ID = "post_id"

        const val LAUNCH_MODE_EMBEDED_NO_TOOLBAR = "embeded_no_toolbar"
        const val LAUNCH_MODE_SEPARATE_WITH_TOOLBAR = "separate_with_toolbar"

        fun newInstance(launchMode: String, subredditName: String, postId: String) =
            CommentsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_LAUNCH_MODE, launchMode)
                    putString(ARG_SUBREDDIT_NAME, subredditName)
                    putString(ARG_POST_ID, postId)
                }
            }
    }
}
