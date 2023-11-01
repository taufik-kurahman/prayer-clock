package com.taufik.prayerclock.data.data_source.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "available_regency_city")
data class AvailableRegencyCityEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name:String
)