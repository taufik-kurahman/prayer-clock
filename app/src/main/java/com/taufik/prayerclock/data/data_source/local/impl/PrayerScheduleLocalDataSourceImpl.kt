package com.taufik.prayerclock.data.data_source.local.impl

import com.taufik.prayerclock.data.data_source.local.PrayerScheduleLocalDataSource
import com.taufik.prayerclock.data.data_source.local.db.dao.AvailableRegencyCityDao
import com.taufik.prayerclock.data.data_source.local.db.dao.PrayerScheduleDao
import com.taufik.prayerclock.data.data_source.local.db.entity.AvailableRegencyCityEntity
import com.taufik.prayerclock.data.data_source.local.db.entity.PrayerScheduleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrayerScheduleLocalDataSourceImpl @Inject constructor(
    private val availableRegencyCityDao: AvailableRegencyCityDao,
    private val prayerScheduleDao: PrayerScheduleDao
) : PrayerScheduleLocalDataSource {
    override suspend fun getAvailableRegenciesCitiesCount(): Int {
        return availableRegencyCityDao.getCount()
    }

    override suspend fun insertAllAvailableRegenciesCities(data: List<AvailableRegencyCityEntity>) {
        availableRegencyCityDao.insertAll(data)
    }

    override suspend fun getAvailableRegencyCityId(name: String): String? {
        return availableRegencyCityDao.getId(name)
    }

    override suspend fun insertAllPrayerSchedule(data: List<PrayerScheduleEntity>) {
        prayerScheduleDao.insertAfterTruncate(data)
    }

    override fun getByDate(date: String): Flow<PrayerScheduleEntity?> {
        return prayerScheduleDao.getByDate(date)
    }

    override suspend fun getDate(): String? {
        return prayerScheduleDao.getDate()
    }
}