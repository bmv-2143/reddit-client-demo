package com.example.finalattestationreddit.presentation.bottom_navigation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class ViewBindingFragment<T : ViewBinding> : Fragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cleanUp()
        _binding = null
    }

    protected abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): T

    /**
     * Perform any necessary cleanup tasks (of views and resources) before the fragment's view is
     * destroyed.
     *
     * This method is called in the onDestroyView() method of the base ViewBindingFragment class.
     */
    protected open fun cleanUp() {
        // clean up views and resources in subclasses
    }

    // todo: incorrect not all ViewBindingFragment have toolbar, should create ViewBinding toolbar fragment
    protected fun setActionbarTitle(title : String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }
}