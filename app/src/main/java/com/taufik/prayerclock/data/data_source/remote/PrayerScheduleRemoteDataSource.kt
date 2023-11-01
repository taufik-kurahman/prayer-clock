package com.taufik.prayerclock.data.data_source.remote

import com.taufik.prayerclock.data.dto.AvailableRegencyCityResponseDto
import com.taufik.prayerclock.data.dto.PrayerScheduleResponseDto

interface PrayerScheduleRemoteDataSource {
    suspend fun getAvailableRegenciesCities(): List<AvailableRegencyCityResponseDto>

    suspend fun getMonthlySchedules(
        regencyCityId: String,
        year: String,
        month: String
    ): PrayerScheduleResponseDto
}