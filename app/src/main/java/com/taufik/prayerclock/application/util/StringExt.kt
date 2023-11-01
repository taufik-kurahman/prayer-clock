package com.taufik.prayerclock.application.util

import java.text.SimpleDateFormat
import java.util.Locale

fun String?.getDateFormatted(
    inputFormat: String = "HH:mm",
    outputFormat: String = "HH",
    localize: Locale = Locale.ENGLISH
): String {
    return try {
        SimpleDateFormat(inputFormat, localize).parse(this.orEmpty())?.let { date ->
            SimpleDateFormat(outputFormat, localize).format(date)
        }.orEmpty()
    } catch (e: java.lang.Exception) {
        ""
    }
}

fun String?.orDefault(default: String) = this ?: default