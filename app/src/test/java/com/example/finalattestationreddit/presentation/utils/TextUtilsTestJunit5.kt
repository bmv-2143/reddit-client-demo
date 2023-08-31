package com.example.finalattestationreddit.presentation.utils

import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.HUNDRED
import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.MILLION
import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.THOUSAND
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// Junit 5 sample tests (see also the same tests implemented using Junit 4)
internal class TextUtilsTestJunit5 {

    @Nested
    inner class FormatLargeNumberTests {

        @Test
        fun `formats millions correctly`() {
            assertEquals("1.0M", formatLargeNumber(MILLION))
            assertEquals("-1.0M", formatLargeNumber(-MILLION))
            assertEquals("1.2M", formatLargeNumber((1.2 * MILLION).toInt()))
            assertEquals("-1.2M", formatLargeNumber((-1.2 * MILLION).toInt()))
        }

        @Test
        fun `formats thousands correctly`() {
            assertEquals("1.0k", formatLargeNumber(THOUSAND))
            assertEquals("-1.0k", formatLargeNumber(-THOUSAND))
            assertEquals("1.2k", formatLargeNumber((1.2 * THOUSAND).toInt()))
            assertEquals("-1.2k", formatLargeNumber((-1.2 * THOUSAND).toInt()))
        }

        @Test
        fun `formats hundreds correctly`() {
            assertEquals("100", formatLargeNumber(HUNDRED))
            assertEquals("-100", formatLargeNumber(-HUNDRED))
            assertEquals("120", formatLargeNumber((1.2 * HUNDRED).toInt()))
            assertEquals("-120", formatLargeNumber((-1.2 * HUNDRED).toInt()))
        }

        @Test
        fun `formats zero correctly`() {
            assertEquals("0", formatLargeNumber(0))
        }
    }
}