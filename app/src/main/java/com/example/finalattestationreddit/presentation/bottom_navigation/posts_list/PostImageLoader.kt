package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.post.Post
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.utils.GlideRequestListenerFactory
import com.example.finalattestationreddit.presentation.utils.ImageUrlExtractor

class PostImageLoader(
    private val context: Context,
    private val targetImageView: ImageView,
    private val onPreLoadAction: (() -> Unit)? = null,
    private val onComplete: (() -> Unit)? = null,
    private val onLoadFailed: (() -> Unit)? = null
) {

    fun loadImage(postItem: Post) {
        val imageUrl = postItem.getFirstUrlOrNull()?.let {
            ImageUrlExtractor.extractBaseImageUrl(it)
        }

        if (imageUrl == null) {
            onLoadFailed?.invoke()
        } else {
            onPreLoadAction?.invoke()
            loadPostImage(imageUrl)
        }
    }

    private fun loadPostImage(imageUrl: String) {
        val failListener = GlideRequestListenerFactory.makeReadyFailListener(
            onResourceReady = onComplete,

            onLoadFailed = {
                Log.e(TAG, "Glide onLoadFailed: $imageUrl")
                onLoadFailed?.invoke()
            }
        )

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.list_item_post_image_placeholder_24)
            .listener(failListener)
            .into(targetImageView)
    }

}