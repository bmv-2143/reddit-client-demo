package com.example.finalattestationreddit.presentation.bottom_navigation.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.databinding.FragmentUserProfileBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.image_utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileFragment : ViewBindingFragment<FragmentUserProfileBinding>() {

    private val viewModel: UserProfileViewModel by viewModels()
    private val activityViewModel: BottomNavigationViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserProfileBinding =
        FragmentUserProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()
        observerUserData()
        observerClearSavedPosts()
        setClearSavedPostsClickListener()
        setExitClickListener()
        setListOfFriendsClickListener()
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

    private fun setClearSavedPostsClickListener() {
        binding.fragmentUserProfileButtonClearSavedPosts.setOnClickListener {
            binding.fragmentUserProfileButtonClearSavedPostsProgressBar.visibility = View.VISIBLE
            viewModel.clearSavedPosts()
        }
    }

    private fun observerClearSavedPosts() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.clearSavedPostsFlow.collectLatest { clearSuccess ->
                    notifyUserOnClearSavedPosts(clearSuccess)
                }
            }
        }
    }

    private fun notifyUserOnClearSavedPosts(clearSuccess: Boolean) {

        fun getClearSavedPostsResultText(clearSuccess: Boolean) =
            if (clearSuccess) {
                getString(R.string.fragment_user_profile_toast_clear_saved_posts_success)
            } else {
                getString(R.string.fragment_user_profile_toast_clear_saved_posts_fail)
            }

        binding.fragmentUserProfileButtonClearSavedPostsProgressBar.visibility = View.GONE

        Toast.makeText(
            requireContext(),
            getClearSavedPostsResultText(clearSuccess),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setExitClickListener() {
        binding.fragmentUserProfileExit.setOnClickListener {
            activityViewModel.logout()
        }
    }

    private fun setListOfFriendsClickListener() {
        binding.fragmentUserProfileButtonListOfFriends.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_user_profile_to_friendsListFragment)
        }
    }
}