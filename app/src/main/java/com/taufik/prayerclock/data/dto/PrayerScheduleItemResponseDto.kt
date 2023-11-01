package com.taufik.prayerclock.data.dto

data class PrayerScheduleItemResponseDto(
    val tanggal: String?,
    val imsak: String?,
    val subuh: String?,
    val terbit: String?,
    val dhuha: String?,
    val dzuhur: String?,
    val ashar: String?,
    val maghrib: String?,
    val isya: String?,
    val date: String?
)
