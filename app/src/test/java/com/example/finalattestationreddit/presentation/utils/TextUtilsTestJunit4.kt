package com.example.finalattestationreddit.presentation.utils

import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.HUNDRED
import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.MILLION
import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.THOUSAND
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

// Junit 4 sample tests (see also the same tests implemented using Junit 5)
internal class TextUtilsTestJunit4 {

    @Test
    fun formatLargeNumber_formatsMillionsCorrectly() {
        assertEquals("1.0M", formatLargeNumber(MILLION))
        assertEquals("-1.0M", formatLargeNumber(-MILLION))
        assertEquals("1.2M", formatLargeNumber((1.2 * MILLION).toInt()))
        assertEquals("-1.2M", formatLargeNumber((-1.2 * MILLION).toInt()))
    }

    @Test
    fun formatLargeNumber_formatsThousandsCorrectly() {

        // Hamcrest matchers sample
        assertThat("1.0k", `is`(formatLargeNumber(THOUSAND)))
        assertThat("-1.0k", `is`(formatLargeNumber(-THOUSAND)))
        assertThat("1.2k", `is`(formatLargeNumber((1.2 * THOUSAND).toInt())))
        assertThat("-1.2k", `is`(formatLargeNumber((-1.2 * THOUSAND).toInt())))
    }

    @Test
    fun formatLargeNumber_formatsHundredsCorrectly() {
        assertEquals("100", formatLargeNumber(HUNDRED))
        assertEquals("-100", formatLargeNumber(-HUNDRED))
        assertEquals("120", formatLargeNumber((1.2 * HUNDRED).toInt()))
        assertEquals("-120", formatLargeNumber((-1.2 * HUNDRED).toInt()))
    }

    @Test
    fun formatLargeNumber_formatsZeroCorrectly() {
        assertEquals("0", formatLargeNumber(0))
    }
}

