package com.example.finalattestationreddit.domain.users

import com.example.finalattestationreddit.data.model.dto.user.User
import com.example.finalattestationreddit.data.repositories.RedditRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class GetMyUserUseCaseTest {

    private val redditRepository = mockk<RedditRepository>()
    private val getMyUserUseCase = GetMyUserUseCase(redditRepository)

    @Test
    fun `getMyUserUseCase returns user provided by repository`() = runTest {

        // arrange
        val expectedUser = TEST_USER_1
        coEvery { redditRepository.getMyUser() } returns expectedUser

        // act
        val actualUser = getMyUserUseCase.invoke()

        // assert
        assertEquals(expectedUser, actualUser)
    }

    @Test
    fun `getMyUserUseCase doesn't return user not from repository`() = runTest {

        // arrange
        val expectedUser = TEST_USER_1
        coEvery { redditRepository.getMyUser() } returns expectedUser

        // act
        val actualUser = getMyUserUseCase.invoke()

        // assert
        val differentUser = TEST_USER_2
        assertNotEquals(differentUser, actualUser)
    }

    companion object {

        private val TEST_USER_1 = User(
            name = "testName_1",
            id = "testId",
            createdUtc = 123456789L,
            totalKarma = 100,
            commentKarma = 50,
            iconImg = "testIconImg",
            friendsNum = 10
        )

        private val TEST_USER_2 = User(
            name = "testName_2",
            id = "testId_2",
            createdUtc = 987654321L,
            totalKarma = 200,
            commentKarma = 100,
            iconImg = "testIconImg_2",
            friendsNum = 20
        )

    }
}