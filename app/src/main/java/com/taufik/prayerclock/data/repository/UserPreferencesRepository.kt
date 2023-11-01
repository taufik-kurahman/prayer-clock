package com.taufik.prayerclock.data.repository

import androidx.datastore.preferences.core.Preferences
import com.taufik.prayerclock.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>

    suspend fun writeFloat(key: Preferences.Key<Float>, value: Float)

    suspend fun writeFloat(data: List<Pair<Preferences.Key<Float>, Float>>)

    suspend fun writeBoolean(key: Preferences.Key<Boolean>, value: Boolean)

    suspend fun writeBoolean(data: List<Pair<Preferences.Key<Boolean>, Boolean>>)

    suspend fun writeString(key: Preferences.Key<String>, value: String)

    suspend fun writeString(data: List<Pair<Preferences.Key<String>, String>>)

    suspend fun resetColors()

    suspend fun getLastRegencyCity(): String?
}