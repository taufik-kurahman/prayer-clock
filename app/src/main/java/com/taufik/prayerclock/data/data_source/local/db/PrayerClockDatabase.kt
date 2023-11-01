package com.taufik.prayerclock.data.data_source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taufik.prayerclock.data.data_source.local.db.dao.AvailableRegencyCityDao
import com.taufik.prayerclock.data.data_source.local.db.dao.PrayerScheduleDao
import com.taufik.prayerclock.data.data_source.local.db.entity.AvailableRegencyCityEntity
import com.taufik.prayerclock.data.data_source.local.db.entity.PrayerScheduleEntity

@Database(entities = [AvailableRegencyCityEntity::class, PrayerScheduleEntity::class], version = 1, exportSchema = false)
abstract class PrayerClockDatabase : RoomDatabase() {
    abstract fun availableRegencyCityDao(): AvailableRegencyCityDao
    abstract fun prayerScheduleDao(): PrayerScheduleDao
}