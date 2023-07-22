package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.ListItemPostBinding
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.log.TAG
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
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(postItem: Post) {
        currentItem = postItem

        hidePostContentIfRequired(postItem)
        loadTexts(postItem)
        loadImageOrHide(postItem)
    }

    private fun hidePostContentIfRequired(postItem: Post) {
        if (shouldHidePostContent(postItem)) {
            binding.listItemPostContent.visibility = View.GONE
        } else {
            binding.listItemPostContent.visibility = View.VISIBLE
        }
    }

    private fun loadImageOrHide(postItem: Post) {
        val imageUrl = postItem.getFirstUrlOrNull()?.let { extractBaseImageUrl(it) }

        if (imageUrl == null) {
            hideImage()
        } else {
            binding.listItemPostTextBodyGuideline.setGuidelinePercent(
                IMAGE_AND_TEXT_GUIDELINE_PERCENT
            )
            loadPostImage(imageUrl, ::hideImage)
        }
    }

    private fun Post.getFirstUrlOrNull(): String? = preview?.images?.firstOrNull()?.source?.url

    private fun hideImage() =
        binding.listItemPostTextBodyGuideline.setGuidelinePercent(NO_IMAGE_GUIDELINE_PERCENT)

    private fun loadPostImage(imageUrl: String, onLoadFailed: () -> Unit = {}) {
        Glide.with(binding.root.context)
            .load(imageUrl)
            .placeholder(R.drawable.list_item_post_image_placeholder_24)
            .listener(object : RequestListener<Drawable> {

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
//                    progressBarSetVisible(binding, false)
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "Glide onLoadFailed: $imageUrl")
                    onLoadFailed()
                    return false
                }
            })
            .into(binding.listItemPostImage)
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


    private fun shouldHideImage(postItem: Post) : Boolean = postItem.getFirstUrlOrNull()
        ?.let { extractBaseImageUrl(it) } == null


    companion object {
        private const val IMAGE_AND_TEXT_GUIDELINE_PERCENT = 0.3f
        private const val NO_IMAGE_GUIDELINE_PERCENT = 0.0f
    }
}
