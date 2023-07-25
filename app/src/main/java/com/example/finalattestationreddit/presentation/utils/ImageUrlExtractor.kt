package com.example.finalattestationreddit.presentation.utils

import android.net.Uri
import android.webkit.URLUtil

// todo: move to another package
// todo: inject is, don't use object
object ImageUrlExtractor {

    private const val UNSUPPORTED_URL_WORD_EXTERNAL = "external"
    private const val URL_WORD_REPLACE_WHAT = "preview"
    private const val URL_WORD_REPLACE_TO = "i"

    internal fun extractBaseImageUrl(fullUrl: String): String? {
        if (isInvalidUrl(fullUrl)) {
            return null
        }
        val baseUrl = extractBaseUrl(fullUrl)
        return convertPreviewToImgUrl(baseUrl)
    }

    private fun extractBaseUrl(url: String): String {
        val uri = Uri.parse(url)
        return Uri.Builder()
            .scheme(uri.scheme)
            .authority(uri.authority)
            .path(uri.path)
            .build()
            .toString()
    }

    private fun isInvalidUrl(url: String?) =
        url == null || url.contains(UNSUPPORTED_URL_WORD_EXTERNAL) || !URLUtil.isValidUrl(url)


    private fun convertPreviewToImgUrl(baseUrl: String) =
        baseUrl.replace(URL_WORD_REPLACE_WHAT, URL_WORD_REPLACE_TO)

}