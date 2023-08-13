package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

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
import com.example.finalattestationreddit.presentation.utils.GlideRequestListenerFactory
import com.example.finalattestationreddit.presentation.utils.ImageUrlExtractor
import com.example.finalattestationreddit.presentation.utils.ShareUtils
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

    @Inject
    lateinit var shareUtils: ShareUtils

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditInfoBinding =
        FragmentSubredditInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.selectedSubredditFlow.value?.let { subredditData ->
            initToolbar(subredditData.displayNamePrefixed)
            startLoadSubreddit(subredditData.displayName)
            observeSubredditFlow()

            setShareButtonClickListener(subredditData)
            setSubscribeButtonClickListener()
            observerSubscriptionUpdatesFlow()
            loadSubredditIcon(subredditData)
        }
    }

    private fun setInitialUiState(subredditData: SubredditData) {
        setSubredditDescription(subredditData)
        setSubredditSubscriptionStatus(subredditData)
    }

    private fun loadSubredditIcon(subredditData: SubredditData) {
        val imageUrl = ImageUrlExtractor.extractBaseImageUrl(subredditData.communityIcon)
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.fragment_subreddit_info_frog_face_primary_color)
            .circleCrop()
            .listener(
                GlideRequestListenerFactory.makeOperationEndListener(::hideProgressBar)
            )
            .into(binding.fragmentSubredditInfoImage)
    }

    private fun hideProgressBar() {
        binding.fragmentSubredditInfoProgressBar.visibility = View.GONE
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
        subredditData.userIsSubscriber?.let { userIsSubscriber ->
            binding.fragmentSubredditInfoButtonSubscribe.isChecked = userIsSubscriber
        }
    }

    private fun setShareButtonClickListener(subredditData: SubredditData) {
        binding.fragmentSubredditInfoButtonShare.setOnClickListener {
            val shareUrl = viewModel.getSubredditUrl(subredditData)
            shareUtils.shareUrl(
                shareUrl,
                getString(R.string.fragment_subreddit_info_share_chooser_title)
            )
        }
    }

    private fun startLoadSubreddit(subredditDisplayName: String) {
        viewModel.loadSubreddit(subredditDisplayName)
    }

    private fun observeSubredditFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subredditFlow.collectLatest { subredditData ->
                    if (subredditData != null) {
                        setSubredditSubscriptionStatus(subredditData)
                        setInitialUiState(subredditData)
                    }
                }
            }
        }
    }

    private fun setSubscribeButtonClickListener() {
        binding.fragmentSubredditInfoButtonSubscribe.setOnClickListener {
            updateSubscription()
        }
    }

    private fun updateSubscription() {
        viewModel.subredditFlow.value?.let {
            activityViewModel.switchSubscription(it)
        }
    }

    private fun observerSubscriptionUpdatesFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.subscriptionUpdatesFlow.collectLatest { subredditData ->
                    viewModel.setSubreddit(subredditData)
                    setSubredditSubscriptionStatus(subredditData)
                }
            }
        }
    }

}