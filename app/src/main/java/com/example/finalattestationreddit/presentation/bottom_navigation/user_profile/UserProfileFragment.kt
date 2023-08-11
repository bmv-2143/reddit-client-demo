package com.example.finalattestationreddit.presentation.bottom_navigation.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.databinding.FragmentUserProfileBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.image_utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : ViewBindingFragment<FragmentUserProfileBinding>() {

    private val viewModel: UserProfileViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserProfileBinding =
        FragmentUserProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()
        observerUserData()
    }

    private fun loadUserData() = viewModel.getUser()

    private fun observerUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFlow.filterNotNull().collectLatest { user ->
                    updateTexts(user)
                    user.iconImg?.let { loadUserAvatar(it) }
                    loadingUserPostsCount(user.name)
                }
            }
        }
    }

    private fun updateTexts(user: User) {
        binding.fragmentProfileUserName.text = user.name
        binding.fragmentUserProfileKarma.text =
            getString(R.string.fragment_user_profile_karma, user.totalKarma)
        binding.fragmentUserProfileNumberOfFriends.text =
            getString(R.string.fragment_user_profile_number_of_friends, user.friendsNum)
    }

    private fun loadUserAvatar(avatarUrl: String) = ImageUtils()
        .loadCircularAvatar(requireContext(), avatarUrl, binding.fragmentUserProfileUserAvatar)

    private fun loadingUserPostsCount(username: String) = viewModel.loadUserPostsCount(username)

}