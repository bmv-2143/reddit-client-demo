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
    fun `test getMyUser returns expected user`() = runTest {

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
        coEvery { redditRepository.getMyUser() } returns expectedUser

        // act
        val actualUser = getMyUserUseCase.invoke()

        // assert
        assertEquals(expectedUser, actualUser)
    }


    @Test
    fun `test getMyUser does not return different user`() = runTest {

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
        coEvery { redditRepository.getMyUser() } returns expectedUser

        // act
        val actualUser = getMyUserUseCase.invoke()

        // assert
        val differentUser = User(
            name = "differentName",
            id = "differentId",
            createdUtc = 987654321L,
            totalKarma = 200,
            commentKarma = 100,
            iconImg = "differentIconImg",
            friendsNum = 20
        )
        assertNotEquals(differentUser, actualUser)
    }
}