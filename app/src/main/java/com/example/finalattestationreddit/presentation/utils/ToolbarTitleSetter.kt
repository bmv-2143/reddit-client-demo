package com.example.finalattestationreddit.presentation.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ToolbarTitleSetter @Inject constructor(
    @ActivityContext private val context: Context
)  {
    fun setToolbarTitle(title: String) {
        (context as? AppCompatActivity)?.supportActionBar?.title = title
    }
}