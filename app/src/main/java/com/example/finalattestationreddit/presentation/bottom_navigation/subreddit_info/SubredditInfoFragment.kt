package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.subreddit.SubredditData
import com.example.finalattestationreddit.databinding.FragmentSubredditInfoBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListFragmentArgs
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SubredditInfoFragment : ViewBindingFragment<FragmentSubredditInfoBinding>() {

    private val viewModel : SubredditInfoViewModel by viewModels()

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditInfoBinding =
        FragmentSubredditInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

        val subredditData = PostsListFragmentArgs.fromBundle(requireArguments()).subredditData
        toolbarTitleSetter.setToolbarTitle(subredditData.displayNamePrefixed)
        setSubredditDescription(subredditData)
        setSubredditSubscriptionStatus(subredditData)
        setShareButtonClickListener(subredditData)
    }

    private fun initToolbar() {
        val activity = (requireActivity() as AppCompatActivity)
        activity.setSupportActionBar(binding.fragmentSubredditInfoToolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setSubredditDescription(subredditData: SubredditData) {
        binding.fragmentSubredditInfoDescription.text = subredditData.publicDescription
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

    private fun shareSubreddit(shareUrl : String) {
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

    companion object {
        private const val INTENT_MIME_TYPE_PLAIN_TEXT = "text/plain"
    }
}