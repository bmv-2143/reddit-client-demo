package com.example.finalattestationreddit.presentation.bottom_navigation.posts_list

import android.net.Uri
import com.example.finalattestationreddit.data.dto.post.Post

object ImageUrlExtractor {

    private const val UNSUPPORTED_URL_WORD_EXTERNAL = "external"
    private const val URL_WORD_REPLACE_WHAT = "preview"
    private const val URL_WORD_REPLACE_TO = "i"

    internal fun extractBaseImageUrl(post: Post): String? {
        val url = post.preview?.images?.firstOrNull()?.source?.url

        if (isInvalidUrl(url)) {
            return null
        }
        val baseUrl = extractBaseUrl(url!!)
        return convertPreviewToImgUrl(baseUrl)
    }

    private fun extractBaseUrl(url: String): String {
        val uri = Uri.parse(url)
        return "${uri.scheme}://${uri.authority}${uri.path}"
    }

    private fun isInvalidUrl(url: String?) =
        url == null || url.contains(UNSUPPORTED_URL_WORD_EXTERNAL)


    private fun convertPreviewToImgUrl(baseUrl: String) =
        baseUrl.replace(URL_WORD_REPLACE_WHAT, URL_WORD_REPLACE_TO)

}