package com.example.finalattestationreddit.presentation.utils

import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.finalattestationreddit.log.TAG

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
                executeCatching(onResourceReady)
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                executeCatching(onLoadFailed)
                return false
            }
        }
    }

    private fun executeCatching(action: (() -> Unit)? = null) {
        try {
            action?.invoke()
        } catch (e: NullPointerException) {
            Log.e(TAG,
                "Glide callback execution error: execution context is null: $e")
        }
    }
}

