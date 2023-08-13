package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.lifecycle.Lifecycle
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.FragmentSubredditsSearchBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubredditsSearchFragment : ViewBindingFragment<FragmentSubredditsSearchBinding>() {

    private lateinit var searchMenuProvider: SearchMenuProvider

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSubredditsSearchBinding {
        return FragmentSubredditsSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        intiToolbar()
        initSearchMenuProvider(arguments?.getString("search_query"))
        addActionBarMenu()
    }

    private fun intiToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentSubredditsSearchToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun initSearchMenuProvider(intialQuery: String?) {
        searchMenuProvider = SearchMenuProvider(
            intialQuery,
            onSearchQuerySubmit = { query ->
                Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
            },
            getString(R.string.fragment_subreddits_search_hint)
        )
    }

    private fun addActionBarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(searchMenuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}