package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import android.content.Intent
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
import com.bumptech.glide.Glide
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.databinding.FragmentSubredditInfoBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListFragmentArgs
import com.example.finalattestationreddit.presentation.utils.ImageUrlExtractor
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SubredditInfoFragment : ViewBindingFragment<FragmentSubredditInfoBinding>() {

    private val viewModel: SubredditInfoViewModel by viewModels()
    private val activityViewModel: BottomNavigationViewModel by activityViewModels()

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditInfoBinding =
        FragmentSubredditInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val subredditData = PostsListFragmentArgs.fromBundle(requireArguments()).subredditData
        setInitialUiState(subredditData)
        setShareButtonClickListener(subredditData)
        setSubscribeButtonClickListener(subredditData)
        observerSubscriptionUpdatesFlow()
        startLoadSubreddit(subredditData.displayName)
        observeSubredditFlow()
        loadSubredditIcon(subredditData)
    }

    private fun setInitialUiState(subredditData: SubredditData) {
        initToolbar(subredditData.displayNamePrefixed)
        setSubredditDescription(subredditData)
        setSubredditSubscriptionStatus(subredditData)
    }

    private fun loadSubredditIcon(subredditData: SubredditData) {
        val imageUrl = ImageUrlExtractor.extractBaseImageUrl(subredditData.communityIcon)
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.fragment_subreddit_info_frog_face_primary_color)
            .circleCrop()
            .into(binding.fragmentSubredditInfoImage)
    }

    private fun initToolbar(toolbarTitle: String) {
        val activity = (requireActivity() as AppCompatActivity)
        activity.setSupportActionBar(binding.fragmentSubredditInfoToolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarTitleSetter.setToolbarTitle(toolbarTitle)
    }

    private fun setSubredditDescription(subredditData: SubredditData) {
        binding.fragmentSubredditInfoDescription.text =
            subredditData.publicDescription.ifEmpty {
                getString(R.string.fragment_subreddit_info_no_description)
            }
    }

    private fun setSubredditSubscriptionStatus(subredditData: SubredditData) {
        binding.fragmentSubredditInfoButtonSubscribe.isChecked = subredditData.userIsSubscriber
    }

    private fun setShareButtonClickListener(subredditData: SubredditData) {
        binding.fragmentSubredditInfoButtonShare.setOnClickListener {
            val shareUrl = viewModel.getSubredditUrl(subredditData)
            shareSubreddit(shareUrl)
        }
    }

    private fun setSubscribeButtonClickListener(subredditData: SubredditData) {
        binding.fragmentSubredditInfoButtonSubscribe.setOnClickListener {
            activityViewModel.switchSubscription(subredditData)
        }
    }

    private fun startLoadSubreddit(subredditDisplayName: String) {
        viewModel.loadSubreddit(subredditDisplayName)
    }

    private fun observeSubredditFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subredditFlow.collectLatest { subredditData ->
                    setSubredditSubscriptionStatus(subredditData)
                }
            }
        }
    }

    private fun shareSubreddit(shareUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = INTENT_MIME_TYPE_PLAIN_TEXT
            putExtra(Intent.EXTRA_TEXT, shareUrl)
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.chooser_share_subreddit_title)
            )
        )
    }

    private fun observerSubscriptionUpdatesFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.subscriptionUpdatesFlow.collectLatest { subredditData ->
                    setSubredditSubscriptionStatus(subredditData)
                }
            }
        }
    }

    companion object {
        private const val INTENT_MIME_TYPE_PLAIN_TEXT = "text/plain"
    }
}