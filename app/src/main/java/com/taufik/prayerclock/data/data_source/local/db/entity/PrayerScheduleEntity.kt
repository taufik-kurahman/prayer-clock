package com.taufik.prayerclock.data.data_source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_schedule")
data class PrayerScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "tanggal")
    val tanggal: String,
    @ColumnInfo(name = "imsak")
    val imsak: String,
    @ColumnInfo(name = "subuh")
    val subuh: String,
    @ColumnInfo(name = "terbit")
    val terbit: String,
    @ColumnInfo(name = "dhuha")
    val dhuha: String,
    @ColumnInfo(name = "dzuhur")
    val dzuhur: String,
    @ColumnInfo(name = "ashar")
    val ashar: String,
    @ColumnInfo(name = "maghrib")
    val maghrib: String,
    @ColumnInfo(name = "isya")
    val isya: String,
    @ColumnInfo(name = "date")
    val date: String
)
