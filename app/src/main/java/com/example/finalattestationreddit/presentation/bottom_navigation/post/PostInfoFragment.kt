package com.example.finalattestationreddit.presentation.bottom_navigation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.databinding.FragmentPostInfoBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostImageLoader
import com.example.finalattestationreddit.presentation.utils.TimeUtils
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PostInfoFragment : ViewBindingFragment<FragmentPostInfoBinding>() {

    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

    @Inject
    lateinit var timeUtils: TimeUtils

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostInfoBinding =
        FragmentPostInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        collectSelectedPost()
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
                    toolbarTitleSetter.setToolbarTitle(post.title)
                    updateUi(post)
                }
            }
        }
    }

    private fun updateUi(post: Post) {
        binding.fragmentPostInfoBodyText.text = post.selftext
        binding.fragmentPostInfoAuthor.text = post.author
        binding.fragmentPostInfoPublicationTime.text = timeUtils.formatElapsedTime(post.createdUtc)

        PostImageLoader(
            requireContext(), binding.fragmentPostInfoImage,
            onComplete = ::hideProgressBar,
            onLoadFailed = ::hideProgressBar
        ).loadImage(post)

    }

    private fun hideProgressBar() {
        binding.fragmentPostInfoProgressBar.visibility = View.GONE
    }

}