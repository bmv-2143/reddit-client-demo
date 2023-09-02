package com.example.finalattestationreddit.presentation.bottom_navigation.user_profile

import com.example.finalattestationreddit.data.model.dto.user.User
import com.example.finalattestationreddit.domain.posts.ClearSavedPostUseCase
import com.example.finalattestationreddit.domain.users.GetMyUserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
class UserProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getMyUserUseCase = mockk<GetMyUserUseCase>()
    private val clearSavedPostUseCase = mockk<ClearSavedPostUseCase>()
    private lateinit var userProfileViewModel: UserProfileViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        userProfileViewModel = UserProfileViewModel(getMyUserUseCase, clearSavedPostUseCase)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getUser sets userFlow value`() = runTest {

        // arrange
        val expectedUser = User(
            name = "testName",
            id = "testId",
            createdUtc = 123456789L,
            totalKarma = 100,
            commentKarma = 50,
            iconImg = "testIconImg",
            friendsNum = 10
        )
        coEvery { getMyUserUseCase() } returns expectedUser

        // act
        userProfileViewModel.getUser()

        // assert
        advanceUntilIdle()
        val actualUser = userProfileViewModel.userFlow.first()
        assertEquals(expectedUser, actualUser)
    }

    @Test
    fun `test clearSavedPosts emits value to clearSavedPostsFlow`() = runTest {

        // arrange
        coEvery { clearSavedPostUseCase() } returns true

        // act
        userProfileViewModel.clearSavedPosts()

        // assert
        val actualValue = userProfileViewModel.clearSavedPostsFlow.first()
        assertEquals(true, actualValue)
    }

}