package com.taufik.prayerclock.data.repository

import com.taufik.prayerclock.data.data_source.local.db.entity.AvailableRegencyCityEntity
import com.taufik.prayerclock.data.data_source.local.db.entity.PrayerScheduleEntity
import com.taufik.prayerclock.data.dto.AvailableRegencyCityResponseDto
import com.taufik.prayerclock.data.dto.PrayerScheduleResponseDto
import kotlinx.coroutines.flow.Flow

interface PrayerScheduleRepository {
    suspend fun getAvailableRegenciesCities(): List<AvailableRegencyCityResponseDto>

    suspend fun getAvailableRegenciesCitiesCount(): Int

    suspend fun insertAvailableRegenciesCities(data: List<AvailableRegencyCityEntity>)

    suspend fun getAvailableRegencyCityId(name: String): String?

    suspend fun getMonthlySchedules(
        regencyCityId: String,
        year: String,
        month: String
    ): PrayerScheduleResponseDto

    suspend fun insertAllPrayerSchedule(data: List<PrayerScheduleEntity>)

    fun getPrayerScheduleByDate(date: String): Flow<PrayerScheduleEntity?>

    suspend fun getDate(): String?

    suspend fun getOneShotPrayerScheduleByDate(date: String): PrayerScheduleEntity?
}