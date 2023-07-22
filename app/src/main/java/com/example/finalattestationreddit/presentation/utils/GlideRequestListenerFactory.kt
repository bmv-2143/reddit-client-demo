package com.example.finalattestationreddit.presentation.utils

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

object GlideRequestListenerFactory {

    fun makeOperationEndListener(onOperationEnd: (() -> Unit)) : RequestListener<Drawable> {
        return makeReadyFailListener(
            onResourceReady = onOperationEnd,
            onLoadFailed = onOperationEnd
        )
    }

    fun makeReadyFailListener(
        onResourceReady: (() -> Unit)? = null,
        onLoadFailed: (() -> Unit)? = null,
    ): RequestListener<Drawable> {

        return object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onResourceReady?.invoke()
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadFailed?.invoke()
                return false
            }
        }
    }

}

