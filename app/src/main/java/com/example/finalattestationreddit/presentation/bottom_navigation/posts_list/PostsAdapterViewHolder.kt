package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.databinding.ListItemPostBinding
import com.example.finalattestationreddit.presentation.utils.ImageUrlExtractor.extractBaseImageUrl

class PostsAdapterViewHolder(
    private val binding: ListItemPostBinding,
    private val onClick: (Post) -> Unit,
    private val onItemSubscribeButtonClick: (Post) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: Post? = null

    init {
        setPostsListItemClickListener()
    }

    private fun setPostsListItemClickListener() {
        binding.root.setOnClickListener {
            currentItem?.let(onClick)
        }
    }

    fun bind(postItem: Post) {
        currentItem = postItem

        hidePostContentIfRequired(postItem)
        loadTexts(postItem)

        with(binding) {
            PostImageLoader(root.context, listItemPostImage,
                onPreLoadAction = {
                    listItemPostTextBodyGuideline.setGuidelinePercent(
                        IMAGE_AND_TEXT_GUIDELINE_PERCENT
                    )
                },
                onLoadFailed = {
                    listItemPostTextBodyGuideline.setGuidelinePercent(NO_IMAGE_GUIDELINE_PERCENT)
                }
            ).loadImage(postItem)
        }
    }

    private fun hidePostContentIfRequired(postItem: Post) {
        if (shouldHidePostContent(postItem)) {
            binding.listItemPostContent.visibility = View.GONE
        } else {
            binding.listItemPostContent.visibility = View.VISIBLE
        }
    }

    private fun loadTexts(postItem: Post) {
        binding.listItemPostDisplayName.text = postItem.title
        loadPostBodyTextOrHide(postItem)
    }

    private fun loadPostBodyTextOrHide(postItem: Post) {
        val postContent = postItem.selftext.trim()
        if (postContent.isEmpty()) {
            binding.listItemTextVisibilityControlGroup.visibility = View.GONE
        } else {
            binding.listItemTextVisibilityControlGroup.visibility = View.VISIBLE
            binding.listItemPostDescription.text = postContent
        }
    }

    private fun shouldHidePostContent(postItem: Post): Boolean =
        shouldHidePostText(postItem) && shouldHideImage(postItem)

    private fun shouldHidePostText(postItem: Post) =
        postItem.selftext.trim().isEmpty()


    private fun shouldHideImage(postItem: Post): Boolean = postItem.getFirstUrlOrNull()
        ?.let { extractBaseImageUrl(it) } == null


    companion object {
        private const val IMAGE_AND_TEXT_GUIDELINE_PERCENT = 0.3f
        private const val NO_IMAGE_GUIDELINE_PERCENT = 0.0f
    }
}

