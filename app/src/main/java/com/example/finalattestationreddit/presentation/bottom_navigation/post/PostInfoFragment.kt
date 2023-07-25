package com.example.finalattestationreddit.presentation.bottom_navigation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.databinding.FragmentPostInfoBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostImageLoader
import com.example.finalattestationreddit.presentation.utils.ShareUtils
import com.example.finalattestationreddit.presentation.utils.TimeUtils
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostInfoFragment : ViewBindingFragment<FragmentPostInfoBinding>() {

    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()
    private val viewModel: PostInfoViewModel by viewModels()

    @Inject
    lateinit var timeUtils: TimeUtils

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    @Inject
    lateinit var shareUtils: ShareUtils

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostInfoBinding =
        FragmentPostInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        collectSelectedPost()
        setShareButtonListener()
        setVoteControlsListeners()
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
                activityViewModel.selectedPostFlow.collectLatest { post ->
                    if (post != null) {
                        updateUi(post)
                    }
                }
            }
        }
    }

    private fun updateUi(post: Post) {
        toolbarTitleSetter.setToolbarTitle(post.title)
        updatePostTexts(post)
        updatePostVoteControls(post)
        loadPostImage(post)
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
            true -> {
                binding.fragmentPostInfoScoreVoting.upVote()
            }

            false -> {
                binding.fragmentPostInfoScoreVoting.downVote()
            }

            null -> {
                //                binding.fragmentPostInfoScoreVoting.reset()
            }
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
            val post = activityViewModel.selectedPostFlow.value
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
            val post = activityViewModel.selectedPostFlow.value
            if (post != null)
                viewModel.downVote(post)

        }

        binding.fragmentPostInfoScoreVoting.onUpVoteClickListener = {
            val post = activityViewModel.selectedPostFlow.value
            if (post != null)
                viewModel.upVote(post)
        }
    }

}