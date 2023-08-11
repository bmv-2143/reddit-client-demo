package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.lifecycle.Lifecycle
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.databinding.FragmentFriendsListBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FriendsListFragment : ViewBindingFragment<FragmentFriendsListBinding>() {

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFriendsListBinding =
        FragmentFriendsListBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    private fun initToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.fragmentFriendsListToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarTitleSetter.setToolbarTitle(getString(R.string.fragment_friends_list_toolbar_title))
        initToolbarMenu()
    }

    private fun initToolbarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            FriendsListMenuProvider(onMenuItemClick = ::showMenuItemClickMsg),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun showMenuItemClickMsg() =
        Toast.makeText(
            requireContext(),
            getString(R.string.fragment_friends_list_menu_item_dummy_info_click),
            Toast.LENGTH_SHORT
        ).show()

}