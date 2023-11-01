package com.taufik.prayerclock.data.repository.impl

import com.taufik.prayerclock.data.data_source.local.PrayerScheduleLocalDataSource
import com.taufik.prayerclock.data.data_source.local.db.entity.AvailableRegencyCityEntity
import com.taufik.prayerclock.data.data_source.local.db.entity.PrayerScheduleEntity
import com.taufik.prayerclock.data.data_source.remote.PrayerScheduleRemoteDataSource
import com.taufik.prayerclock.data.dto.AvailableRegencyCityResponseDto
import com.taufik.prayerclock.data.dto.PrayerScheduleResponseDto
import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PrayerScheduleRepositoryImpl @Inject constructor(
    private val prayerScheduleRemoteDataSource: PrayerScheduleRemoteDataSource,
    private val prayerScheduleLocalDataSource: PrayerScheduleLocalDataSource
) : PrayerScheduleRepository {
    override suspend fun getAvailableRegenciesCities(): List<AvailableRegencyCityResponseDto> {
        return prayerScheduleRemoteDataSource.getAvailableRegenciesCities()
    }

    override suspend fun getAvailableRegenciesCitiesCount(): Int {
        return prayerScheduleLocalDataSource.getAvailableRegenciesCitiesCount()
    }

    override suspend fun insertAvailableRegenciesCities(data: List<AvailableRegencyCityEntity>) {
        prayerScheduleLocalDataSource.insertAllAvailableRegenciesCities(data)
    }

    override suspend fun getAvailableRegencyCityId(name: String): String? {
        return prayerScheduleLocalDataSource.getAvailableRegencyCityId(name)
    }

    override suspend fun getMonthlySchedules(
        regencyCityId: String,
        year: String,
        month: String
    ): PrayerScheduleResponseDto {
        return prayerScheduleRemoteDataSource.getMonthlySchedules(
            regencyCityId,
            year,
            month
        )
    }

    override suspend fun insertAllPrayerSchedule(data: List<PrayerScheduleEntity>) {
        prayerScheduleLocalDataSource.insertAllPrayerSchedule(data)
    }

    override fun getPrayerScheduleByDate(date: String): Flow<PrayerScheduleEntity?> {
        return prayerScheduleLocalDataSource.getByDate(date)
    }

    override suspend fun getDate(): String? {
        return prayerScheduleLocalDataSource.getDate()
    }

    override suspend fun getOneShotPrayerScheduleByDate(date: String): PrayerScheduleEntity? {
        return try {
            prayerScheduleLocalDataSource.getByDate(date).first()
        } catch (e: Exception) {
            null
        }
    }
}