package com.taufik.prayerclock.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.taufik.prayerclock.data.data_source.local.db.entity.PrayerScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerScheduleDao {
    @Insert
    suspend fun insert(data: List<PrayerScheduleEntity>)

    @Query("DELETE FROM prayer_schedule")
    suspend fun truncate()

    @Transaction
    suspend fun insertAfterTruncate(data: List<PrayerScheduleEntity>) {
        truncate()
        insert(data)
    }

    @Query("SELECT * FROM prayer_schedule WHERE date = :date")
    fun getByDate(date: String): Flow<PrayerScheduleEntity?>

    @Query("SELECT date from prayer_schedule LIMIT 1")
    suspend fun getDate(): String?
}