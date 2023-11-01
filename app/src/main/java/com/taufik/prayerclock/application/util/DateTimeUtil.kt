package com.taufik.prayerclock.application.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeUtil {
    fun getCurrent(
        pattern: String = "yyyy-MM-dd'T'HH:mm:ss.SSS a",
        locale: Locale = Locale("id", "ID")
    ): String {
        return try {
            SimpleDateFormat(pattern, locale).format(Date())
        } catch (e: Exception) {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a", locale).format(Date())
        }
    }

    fun getTimeDiffMillisOf(hourOfDay: Int, minute: Int = 0): Long {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate[Calendar.HOUR_OF_DAY] = hourOfDay
        dueDate[Calendar.MINUTE] = minute
        dueDate[Calendar.SECOND] = 0
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        return dueDate.timeInMillis - currentDate.timeInMillis
    }
}