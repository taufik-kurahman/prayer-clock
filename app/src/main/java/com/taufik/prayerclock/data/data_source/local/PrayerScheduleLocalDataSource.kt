package com.taufik.prayerclock.data.data_source.local

import com.taufik.prayerclock.data.data_source.local.db.entity.AvailableRegencyCityEntity
import com.taufik.prayerclock.data.data_source.local.db.entity.PrayerScheduleEntity
import kotlinx.coroutines.flow.Flow

interface PrayerScheduleLocalDataSource {
    suspend fun getAvailableRegenciesCitiesCount(): Int

    suspend fun insertAllAvailableRegenciesCities(data: List<AvailableRegencyCityEntity>)

    suspend fun getAvailableRegencyCityId(name: String): String?

    suspend fun insertAllPrayerSchedule(data: List<PrayerScheduleEntity>)

    fun getByDate(date: String): Flow<PrayerScheduleEntity?>

    suspend fun getDate(): String?
}