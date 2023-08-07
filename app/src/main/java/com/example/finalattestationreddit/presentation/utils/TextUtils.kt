package com.example.finalattestationreddit.presentation.utils

import kotlin.math.abs

fun formatLargeNumber(number: Int): String {
    return when {
        abs(number) >= 1_000_000 -> "%.1fM".format(number / 1_000_000.0)
        abs(number) >= 1_000 -> "%.1fk".format(number / 1_000.0)
        else -> number.toString()
    }
}