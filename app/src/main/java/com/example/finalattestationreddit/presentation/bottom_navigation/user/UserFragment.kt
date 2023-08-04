package com.example.finalattestationreddit.presentation.bottom_navigation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.finalattestationreddit.databinding.FragmentUserBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment

class UserFragment : ViewBindingFragment<FragmentUserBinding>() {

    val args : UserFragmentArgs by navArgs()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)

        binding.fragmentUserUserName.text = args.username

        return root
    }

}