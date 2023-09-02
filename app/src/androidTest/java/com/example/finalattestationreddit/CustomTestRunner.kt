package com.example.finalattestationreddit

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/*
    see source:
    https://github.com/android/architecture-samples/blob/views-hilt/app/src/androidTest/java/com/example/android/architecture/blueprints/todoapp/CustomTestRunner.kt
*/

class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}