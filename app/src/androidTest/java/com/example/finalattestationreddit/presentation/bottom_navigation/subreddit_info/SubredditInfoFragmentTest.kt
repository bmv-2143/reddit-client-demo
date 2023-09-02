package com.example.finalattestationreddit.presentation.bottom_navigation.subreddit_info

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.finalattestationreddit.HiltTestActivity
import com.example.finalattestationreddit.R
import com.example.finalattestationreddit.data.model.dto.subreddit.SubredditData
import com.example.finalattestationreddit.domain.subreddits.GetSubredditUseCase
import com.example.finalattestationreddit.launchFragmentInHiltContainer
import com.example.finalattestationreddit.presentation.bottom_navigation.BottomNavigationViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@MediumTest
@RunWith(AndroidJUnit4::class)
internal class SubredditInfoFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activityRule = ActivityScenarioRule(HiltTestActivity::class.java)

    private val getSubredditUseCase = mockk<GetSubredditUseCase>()

    @BindValue
    @JvmField
    val viewModel: SubredditInfoViewModel = SubredditInfoViewModel(getSubredditUseCase)

    @BindValue
    @JvmField
    val bottomNavigationViewModel = mockk<BottomNavigationViewModel>(relaxed = true)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun subredditInfoFragment_whenOpened_subscriptionStatusIsChecked() = runTest {

        // arrange
        prepareMocks(SUBREDDIT_SUBSCRIBED)

        // act
        openSubredditInfoFragment()

        // assert
        onView(withId(R.id.fragment_subreddit_info_button_subscribe)).check(matches(isChecked()))
    }

    @Test
    fun subredditInfoFragment_whenOpened_subscriptionStatusIsNotChecked() = runTest {

        // arrange
        prepareMocks(SUBREDDIT_NOT_SUBSCRIBED)

        // act
        openSubredditInfoFragment()

        // assert
        onView(withId(R.id.fragment_subreddit_info_button_subscribe)).check(matches(isNotChecked()))
    }

    @Test
    fun subredditInfoFragment_whenOpened_subredditPublicDescriptionIsDisplayed() = runTest {

        // arrange
        prepareMocks(SUBREDDIT_SUBSCRIBED)

        // act
        openSubredditInfoFragment()

        // assert
        onView(withId(R.id.fragment_subreddit_info_description))
            .check(matches(withText(SUBREDDIT_SUBSCRIBED.publicDescription)))
    }

    private fun openSubredditInfoFragment() {
        launchFragmentInHiltContainer<SubredditInfoFragment>(
            null,
            R.style.Base_Theme_FinalAttestationRedditNoActionBar,
        )
    }

    private fun prepareMocks(subredditData: SubredditData) {
        coEvery {
            getSubredditUseCase(subredditData.displayName)
        } returns subredditData

        every { bottomNavigationViewModel.selectedSubredditFlow } returns MutableStateFlow(
            subredditData
        )
    }

    companion object {

        private val SUBREDDIT_SUBSCRIBED = SubredditData(
            id = "test_id",
            title = "test_title",
            displayName = "test_display_name",
            url = "test_url",
            communityIcon = "test_community_icon",
            displayNamePrefixed = "test_display_name_prefixed",
            subscribers = 100,
            userIsSubscriber = true,
            publicDescription = "test_public_description"
        )

        private val SUBREDDIT_NOT_SUBSCRIBED = SubredditData(
            id = "test_id",
            title = "test_title",
            displayName = "test_display_name",
            url = "test_url",
            communityIcon = "test_community_icon",
            displayNamePrefixed = "test_display_name_prefixed",
            subscribers = 100,
            userIsSubscriber = false,
            publicDescription = "test_public_description"
        )
    }

}