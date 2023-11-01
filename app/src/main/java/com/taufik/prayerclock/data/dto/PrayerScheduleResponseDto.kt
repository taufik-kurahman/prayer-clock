package com.taufik.prayerclock.data.dto

import com.google.gson.annotations.SerializedName

data class PrayerScheduleResponseDto(
    val id: String?,
    @SerializedName("lokasi")
    val location: String?,
    @SerializedName("daerah")
    val province: String?,
    @SerializedName("koordinat")
    val coordinate: PrayerScheduleCoordinateResponseDto?,
    @SerializedName("jadwal")
    val schedule: List<PrayerScheduleItemResponseDto>?
)
