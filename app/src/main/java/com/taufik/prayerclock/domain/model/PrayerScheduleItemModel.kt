package com.taufik.prayerclock.domain.model

data class PrayerScheduleItemModel(
    val tanggal: String,
    val imsak: String,
    val subuh: String,
    val terbit: String,
    val dhuha: String,
    val dzuhur: String,
    val ashar: String,
    val maghrib: String,
    val isya: String,
    val date: String
) {
    companion object {
        const val DATE_PATTERN = "yyyy-MM-dd"
    }
}
