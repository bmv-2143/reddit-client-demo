package com.example.finalattestationreddit.presentation.bottom_navigation.subreddits

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class VisibleActionBarFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showActionBar()
    }

    private fun showActionBar() {
        (activity as AppCompatActivity).supportActionBar?.show()
//        hideViaFlags()
    }

    @Suppress("DEPRECATION")
    private fun hideViaFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

}