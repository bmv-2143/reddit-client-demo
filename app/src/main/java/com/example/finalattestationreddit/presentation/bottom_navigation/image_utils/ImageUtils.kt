package com.example.finalattestationreddit.presentation.bottom_navigation.image_utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.presentation.utils.ImageUrlExtractor

class ImageUtils {

    internal fun loadCircularAvatar(context: Context, redditAvatarUrl: String, targetImageView: ImageView) {
        val imageUrl = ImageUrlExtractor.extractBaseImageUrl(redditAvatarUrl)
        Glide.with(context)
        .load(imageUrl)
        .placeholder(R.drawable.user_placeholder_person_24)
        .circleCrop()
        .into(targetImageView)
    }

}