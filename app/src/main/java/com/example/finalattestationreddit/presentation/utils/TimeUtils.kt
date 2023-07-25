package com.example.finalattestationreddit.presentation.utils

import android.content.Context
import com.example.finalattestationreddit.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimeUtils @Inject constructor(@ApplicationContext val context: Context) {

    fun formatElapsedTime(createdUtc: Long): String {
        val currentTime = System.currentTimeMillis() / 1000
        val elapsedTime = currentTime - createdUtc

        return when {
            elapsedTime < secondsInMinute -> context.getString(
                R.string.time_seconds_ago,
                elapsedTime
            )

            elapsedTime < secondsInHour -> context.getString(
                R.string.time_minutes_ago,
                elapsedTime / secondsInMinute
            )

            elapsedTime < secondsInDay -> context.getString(
                R.string.time_hours_ago,
                elapsedTime / secondsInHour
            )

            elapsedTime < secondsInWeek -> context.getString(
                R.string.time_days_ago,
                elapsedTime / secondsInDay
            )

            elapsedTime < secondsInMonth -> context.getString(
                R.string.time_weeks_ago,
                elapsedTime / secondsInWeek
            )

            elapsedTime < secondsInYear -> context.getString(
                R.string.time_months_ago,
                elapsedTime / secondsInMonth
            )

            else -> context.getString(
                R.string.time_years_ago, elapsedTime / secondsInYear)

        }
    }

    private val secondsInMinute = 60
    private val secondsInHour = 60 * secondsInMinute
    private val secondsInDay = 24 * secondsInHour
    private val secondsInWeek = 7 * secondsInDay
    private val secondsInMonth = 30 * secondsInDay
    private val secondsInYear = 365 * secondsInDay
}