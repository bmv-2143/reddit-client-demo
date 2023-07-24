package com.example.finalattestationreddit.presentation.bottom_navigation.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.example.finalattestationreddit.databinding.FragmentPostInfoBinding
import com.example.finalattestationreddit.presentation.bottom_navigation.base.ViewBindingFragment
import com.example.finalattestationreddit.presentation.utils.ToolbarTitleSetter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PostInfoFragment : ViewBindingFragment<FragmentPostInfoBinding>() {

    private val navigationArgs: PostInfoFragmentArgs by navArgs()

    @Inject
    lateinit var toolbarTitleSetter: ToolbarTitleSetter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPostInfoBinding =
        FragmentPostInfoBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        toolbarTitleSetter.setToolbarTitle(navigationArgs.postName)
    }

    // todo: DRY, see other toolbar fragments
    private fun initToolbar() {
        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(binding.fragmentPostsListToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}