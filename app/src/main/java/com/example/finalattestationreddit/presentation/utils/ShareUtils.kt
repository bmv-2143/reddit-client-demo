package com.example.finalattestationreddit.presentation.utils

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ShareUtils @Inject constructor(@ApplicationContext val context: Context) {

    internal fun shareUrl(shareUrl: String, shareChooserTitle: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = INTENT_MIME_TYPE_PLAIN_TEXT
            putExtra(Intent.EXTRA_TEXT, shareUrl)
        }
        context.startActivity(
            Intent.createChooser(shareIntent, shareChooserTitle)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    companion object {
        private const val INTENT_MIME_TYPE_PLAIN_TEXT = "text/plain"
    }

}