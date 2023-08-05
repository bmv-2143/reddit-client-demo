package com.example.finalattestationreddit.presentation.bottom_navigation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.dto.user.User
import com.example.finalattestationreddit.databinding.FragmentUserBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : ViewBindingFragment<FragmentUserBinding>() {

    private val args: UserFragmentArgs by navArgs()
    private val viewModel: UserFragmentViewModel by viewModels()

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
        startLoadUserData()
    }

    private fun startLoadUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUser(args.username)
            }
        }
    }

    private fun observerUserData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userFlow.filterNotNull().collectLatest { user ->
                    setUserData(user)
                }

            }
        }
    }

    private fun setUserData(user: User) {
        binding.fragmentUserLinkKarma.text =
            getString(R.string.fragment_user_karma_template, user.linkKarma)

    }

}