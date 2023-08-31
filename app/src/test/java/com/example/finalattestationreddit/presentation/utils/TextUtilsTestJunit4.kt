package com.example.finalattestationreddit.presentation.utils

import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.HUNDRED
import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.MILLION
import com.example.finalattestationreddit.presentation.utils.TestUtilsConsts.THOUSAND
import org.junit.Assert.assertEquals
import org.junit.Test

// Junit 4 sample tests (see also the same tests implemented using Junit 5)
internal class TextUtilsTestJunit4 {

    @Test
    fun formatsMillionsCorrectly() {
        assertEquals("1.0M", formatLargeNumber(MILLION))
        assertEquals("-1.0M", formatLargeNumber(-MILLION))
        assertEquals("1.2M", formatLargeNumber((1.2 * MILLION).toInt()))
        assertEquals("-1.2M", formatLargeNumber((-1.2 * MILLION).toInt()))
    }

    @Test
    fun formatsThousandsCorrectly() {
        assertEquals("1.0k", formatLargeNumber(THOUSAND))
        assertEquals("-1.0k", formatLargeNumber(-THOUSAND))
        assertEquals("1.2k", formatLargeNumber((1.2 * THOUSAND).toInt()))
        assertEquals("-1.2k", formatLargeNumber((-1.2 * THOUSAND).toInt()))
    }

    @Test
    fun formatsHundredsCorrectly() {
        assertEquals("100", formatLargeNumber(HUNDRED))
        assertEquals("-100", formatLargeNumber(-HUNDRED))
        assertEquals("120", formatLargeNumber((1.2 * HUNDRED).toInt()))
        assertEquals("-120", formatLargeNumber((-1.2 * HUNDRED).toInt()))
    }

    @Test
    fun formatsZeroCorrectly() {
        assertEquals("0", formatLargeNumber(0))
    }
}

