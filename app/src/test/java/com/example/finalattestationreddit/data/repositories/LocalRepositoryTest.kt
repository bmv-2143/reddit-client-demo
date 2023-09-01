package com.example.finalattestationreddit.data.repositories

import android.content.SharedPreferences
import com.example.finalattestationreddit.data.repositories.PrefsKeys.Companion.IS_ONBOARDING_SHOWED
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LocalRepositoryTest {

    private val sharedPreferences = mockk<SharedPreferences>()
    private val editor = mockk<SharedPreferences.Editor>(relaxed = true)

    private lateinit var localRepository: LocalRepository

    @BeforeEach
    fun setUp() {
        every { sharedPreferences.edit() } returns editor
        localRepository = LocalRepository(sharedPreferences)
    }

    @Test
    fun `isOnboardingShowed returns true`() {

        // arrange
        every { sharedPreferences.getBoolean(IS_ONBOARDING_SHOWED, false) } returns true

        // act
        val actualValue = localRepository.isOnboardingShowed()

        // assert
        assertEquals(true, actualValue)
    }

    @Test
    fun `isOnboardingShowed returns false`() {

        // arrange
        every { sharedPreferences.getBoolean(IS_ONBOARDING_SHOWED, false) } returns false

        // act
        val actualValue = localRepository.isOnboardingShowed()

        // assert
        assertEquals(false, actualValue)
    }

    @Test
    fun `saveOnboardingShowedStatus sets onboarding showed status`() {

        // act
        localRepository.saveOnboardingShowedStatus()

        // assert
        verify { editor.putBoolean(IS_ONBOARDING_SHOWED, true) }
        verify { editor.apply() }
    }

    @Test
    fun `getAuthRequestState returns request state from shared preferences`() {

        // arrange
        every {
            sharedPreferences.getString(
                PrefsKeys.AUTH_REQUEST_STATE,
                null
            )
        } returns TEST_AUTH_REQUEST_STATE

        // act
        val actualValue = localRepository.getAuthRequestState()

        // assert
        assertEquals(TEST_AUTH_REQUEST_STATE, actualValue)
    }

    @Test
    fun `saveAuthRequestState sets auth request state`() {

        // act
        localRepository.saveAuthRequestState(TEST_AUTH_REQUEST_STATE)

        // assert
        verify { editor.putString(PrefsKeys.AUTH_REQUEST_STATE, TEST_AUTH_REQUEST_STATE) }
        verify { editor.apply() }
    }

    companion object {
        const val TEST_AUTH_REQUEST_STATE = "testAuthRequestState"
    }

}