package com.taufik.prayerclock.data.data_source.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.taufik.prayerclock.data.data_source.local.db.entity.AvailableRegencyCityEntity

@Dao
interface AvailableRegencyCityDao {
    @Query("SELECT COUNT(*) FROM available_regency_city")
    suspend fun getCount(): Int

    @Insert
    suspend fun insertAll(data: List<AvailableRegencyCityEntity>)

    @Query("SELECT id FROM available_regency_city WHERE name = :name")
    suspend fun getId(name: String): String?
}