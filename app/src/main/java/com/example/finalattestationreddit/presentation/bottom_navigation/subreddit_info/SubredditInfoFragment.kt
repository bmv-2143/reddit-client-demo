package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.finalattestationreddit.R
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

        val args = PostsListFragmentArgs.fromBundle(requireArguments())
        toolbarTitleSetter.setToolbarTitle(args.subredditData.displayNamePrefixed)

        setSubredditDescription(args)
        setSubredditSubscriptionStatus(args)
        setShareButtonClickListener(args)
    }

    private fun setSubredditDescription(args: PostsListFragmentArgs) {
        binding.fragmentSubredditInfoDescription.text = args.subredditData.publicDescription
    }

    private fun setSubredditSubscriptionStatus(args: PostsListFragmentArgs) {
        binding.fragmentSubredditInfoButtonSubscribe.isChecked = args.subredditData.userIsSubscriber
    }

    private fun setShareButtonClickListener(args: PostsListFragmentArgs) {
        binding.fragmentSubredditInfoButtonShare.setOnClickListener {
            val shareUrl = viewModel.getSubredditUrl(args.subredditData)
            shareSubreddit(shareUrl)
        }
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.fragmentSubredditInfoToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun shareSubreddit(shareUrl : String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareUrl)
        }
        startActivity(
            Intent.createChooser(
                shareIntent,
                getString(R.string.chooser_share_subreddit_title)
            )
        )
    }
}