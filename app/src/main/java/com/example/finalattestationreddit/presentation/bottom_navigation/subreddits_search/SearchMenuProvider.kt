package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits_search

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import com.example.finalattestationreddit.R

class SearchMenuProvider(
    private val initialSearchQuery: String?,
    private val onSearchQuerySubmit: (query : String) -> Unit,
    private val queryHint : String? = null
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.fragment_subreddits_search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        setOnQueryTextListener(searchView)
        setInitialSearchQuery(searchItem, searchView)
        configureAppearance(searchView)
    }

    private fun configureAppearance(searchView: SearchView) {
        searchView.setIconifiedByDefault(false)
        queryHint?.let {
            searchView.queryHint = queryHint
        }
    }

    private fun setInitialSearchQuery(
        searchItem: MenuItem,
        searchView: SearchView
    ) {
        initialSearchQuery?.let {
            searchItem.expandActionView()
            searchView.setQuery(initialSearchQuery, true)
        }
    }

    private fun setOnQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    onSearchQuerySubmit(query)
                }
                hideSoftInputKeyboard(searchView)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun hideSoftInputKeyboard(searchView: SearchView) {
        val inputMethodManager =
            searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_search -> {
                true
            }

            else -> false
        }
    }
}