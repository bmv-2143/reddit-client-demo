package com.example.finalattestationreddit

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : BaseApp() {

    companion object {
        const val SHARED_PREFERENCES = "sharedPreferences"
    }

}