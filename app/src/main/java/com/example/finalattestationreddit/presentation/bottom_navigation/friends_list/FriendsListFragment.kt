package com.example.finalattestationreddit.presentation.bottom_navigation.friends_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.user.Friend
import com.example.finalattestationreddit.databinding.FragmentFriendsListBinding
import com.example.finalattestationreddit.log.TAG
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FriendsListFragment : ViewBindingFragment<FragmentFriendsListBinding>() {

    private val viewModel : FriendsListViewModel by viewModels()

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    private val friendsListAdapter = FriendsListAdapter(::onFriendItemClick)

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFriendsListBinding =
        FragmentFriendsListBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        binding.fragmentFriendsListRecyclerView.adapter = friendsListAdapter
        binding.fragmentFriendsListRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2)
        observeFriends()
        loadFriends()
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

    private fun loadFriends() {
        viewModel.getFriends()
    }

    private fun observeFriends() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friends.collect { friends ->
                    Log.e(TAG, "observeFriends: $friends")
                    friendsListAdapter.submitList(friends)
                    binding.fragmentPostsListProgressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun onFriendItemClick(friend: Friend) {
        Toast.makeText(
            requireContext(),
            getString(R.string.fragment_friends_list_friend_item_click_msg, friend.name),
            Toast.LENGTH_SHORT
        ).show()
    }
}