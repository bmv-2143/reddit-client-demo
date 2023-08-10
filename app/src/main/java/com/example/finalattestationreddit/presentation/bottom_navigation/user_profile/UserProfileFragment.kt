package com.example.finalattestationreddit.presentation.bottom_navigation.user_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.finalattestationreddit.databinding.FragmentUserProfileBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment

class UserProfileFragment : ViewBindingFragment<FragmentUserProfileBinding>() {

    private val userProfileViewModel: UserProfileViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserProfileBinding =
        FragmentUserProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}