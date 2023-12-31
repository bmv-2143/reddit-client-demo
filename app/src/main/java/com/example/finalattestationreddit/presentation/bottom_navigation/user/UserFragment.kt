package com.example.finalattestationreddit.presentation.bottom_navigation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.model.dto.post.Post
import com.example.finalattestationreddit.data.model.dto.user.User
import com.example.finalattestationreddit.databinding.FragmentUserBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.bottom_navigation.image_utils.ImageUtils
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostItemClickListener
import com.example.finalattestationreddit.presentation.bottom_navigation.posts_list.PostsListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : ViewBindingFragment<FragmentUserBinding>(), PostItemClickListener {

    private val args: UserFragmentArgs by navArgs()
    private val viewModel: UserFragmentViewModel by viewModels()
    private val activityViewModel: BottomNavigationViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragmentUserUserName.text = args.username
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerUserData()
        loadUserData()

        observeUserPostsCount()
        loadingUserPostsCount()

        checkIfUserIsFriend()
        observerIsFriend()

        setAddFriendButtonClickListener()
        setRemoveFriendButtonClickListener()

        addNoToolbarPostsListFragment()
    }

    private fun loadUserData() = viewModel.getUser(args.username)

    private fun observerUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFlow.filterNotNull().collectLatest { user ->
                    updateUiKarma(user)
                    user.iconImg?.let { loadUserAvatar(it) }
                }
            }
        }
    }

    private fun updateUiKarma(user: User) {
        binding.fragmentUserLinkKarma.text =
            getString(R.string.fragment_user_karma_template, user.totalKarma)
    }

    private fun observeUserPostsCount() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userPostsCountFlow.collectLatest { userPostsCount ->
                    setUserPostsCount(userPostsCount)
                }
            }
        }
    }

    private fun setUserPostsCount(userPostsCount: Int) {
        binding.fragmentUserIconTextPostCount.text = userPostsCount.toString()
    }

    private fun loadingUserPostsCount() = viewModel.loadUserPostsCount(args.username)

    private fun setAddFriendButtonClickListener() {
        binding.fragmentPostInfoButtonAddFriend.setOnClickListener {
            viewModel.addFriend(args.username)
        }
    }

    private fun setRemoveFriendButtonClickListener() {
        binding.fragmentPostInfoButtonRemoveFriend.setOnClickListener {
            viewModel.removeFriend(args.username)
        }
    }

    private fun loadUserAvatar(avatarUrl : String) =
        ImageUtils().loadCircularAvatar(requireContext(), avatarUrl, binding.fragmentUserUserAvatar)

    private fun checkIfUserIsFriend() = viewModel.checkIfIsAFriend(args.username)

    private fun observerIsFriend() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFriendFlow.collectLatest { isFriend ->
                    setAddFriendButtonState(isFriend)
                }
            }
        }
    }

    private fun setAddFriendButtonState(isFriend: Boolean) {
        if (isFriend) {
            binding.fragmentPostInfoButtonAddFriend.visibility = View.GONE
            binding.fragmentPostInfoButtonRemoveFriend.visibility = View.VISIBLE
        } else {
            binding.fragmentPostInfoButtonAddFriend.visibility = View.VISIBLE
            binding.fragmentPostInfoButtonRemoveFriend.visibility = View.GONE
        }
    }

    private fun addNoToolbarPostsListFragment() {
        val fragment = PostsListFragment.newUserPostsInstance(
            false, this)
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_user_posts_lists_fragment_container, fragment)
        transaction.commit()
    }

    override fun onPostItemClick(post: Post) {
        activityViewModel.setSelectedPost(post)
        findNavController().navigate(R.id.action_userFragment_to_postInfoFragment)
    }
}