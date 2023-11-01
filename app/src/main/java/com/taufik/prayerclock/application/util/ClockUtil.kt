package com.taufik.prayerclock.application.util

import com.taufik.prayerclock.domain.model.PrayerTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ClockUtil {
    fun calculateAngleForHour(hours: Int, minutes: Int): Float {
        return (((hours % 12) / 12f * 60f) + minutes / 12f) * 360f / 60f
    }

    fun calculateAngleForMinute(minutes: Int, seconds: Int): Float {
        return (minutes + seconds / 60f) * 360f / 60
    }

    fun calculateAngleForSecond(seconds: Int): Float {
        return seconds * 360f / 60f
    }

    fun getNextPrayerSchedule(
        prayerTimes: List<Pair<PrayerTime, String>>,
        currentIndex: Int
    ): Pair<PrayerTime, String> {
        val prayerTimesAsList = prayerTimes.toList()
        return try {
            prayerTimesAsList[currentIndex + 1]
        } catch (e: IndexOutOfBoundsException) {
            prayerTimesAsList.first()
        }
    }

    fun getFormattedString(date: Date, outputPatter: String): String {
        return try {
            SimpleDateFormat(outputPatter, Locale.getDefault()).format(date)
        } catch (e: Exception) {
            ""
        }
    }
}