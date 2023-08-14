package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits

import androidx.appcompat.widget.SearchView

class SearchQuerySubmittedListener(private val onQuerySubmitted: (String) -> Unit) :
    SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            onQuerySubmitted(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}