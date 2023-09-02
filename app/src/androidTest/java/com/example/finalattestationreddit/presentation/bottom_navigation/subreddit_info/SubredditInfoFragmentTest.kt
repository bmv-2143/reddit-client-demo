package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.finalattestationreddit.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@MediumTest
@RunWith(AndroidJUnit4::class)
internal class SubredditInfoFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Test
    fun subredditInfoFragment_OpenFragmentSample() {

        // arrange

        // act
        launchFragmentInHiltContainer<SubredditInfoFragment>(
            null,
            com.example.finalattestationreddit.R.style.Base_Theme_FinalAttestationRedditNoActionBar
        )

        // assert

        Thread.sleep(2000)

    }

}