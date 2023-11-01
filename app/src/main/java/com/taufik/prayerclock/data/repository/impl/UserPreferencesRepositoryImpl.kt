package com.taufik.prayerclock.data.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.taufik.prayerclock.application.util.Logger
import com.taufik.prayerclock.application.util.orDefault
import com.taufik.prayerclock.data.preferences.PreferencesKeys
import com.taufik.prayerclock.data.repository.UserPreferencesRepository
import com.taufik.prayerclock.domain.model.UserPreferences
import com.taufik.prayerclock.presentation.theme.defaultAsharColor
import com.taufik.prayerclock.presentation.theme.defaultDzuhurColor
import com.taufik.prayerclock.presentation.theme.defaultIsyaColor
import com.taufik.prayerclock.presentation.theme.defaultMaghribColor
import com.taufik.prayerclock.presentation.theme.defaultSubuhColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {
    override val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Logger.error(
                    "UserPreferencesRepository",
                    "Error reading preferences.",
                    exception
                )

                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            createUserPreferences(preferences)
        }

    override suspend fun writeFloat(key: Preferences.Key<Float>, value: Float) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun writeFloat(data: List<Pair<Preferences.Key<Float>, Float>>) {
        dataStore.edit { preferences ->
            data.forEach {
                preferences[it.first] = it.second
            }
        }
    }

    override suspend fun writeBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun writeBoolean(data: List<Pair<Preferences.Key<Boolean>, Boolean>>) {
        dataStore.edit { preferences ->
            data.forEach {
                preferences[it.first] = it.second
            }
        }
    }

    override suspend fun writeString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun writeString(data: List<Pair<Preferences.Key<String>, String>>) {
        dataStore.edit { preferences ->
            data.forEach {
                preferences[it.first] = it.second
            }
        }
    }

    override suspend fun resetColors() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SUBUH_COLOR_R] = defaultSubuhColor.red
            preferences[PreferencesKeys.SUBUH_COLOR_G] = defaultSubuhColor.green
            preferences[PreferencesKeys.SUBUH_COLOR_B] = defaultSubuhColor.blue

            preferences[PreferencesKeys.DZUHUR_COLOR_R] = defaultDzuhurColor.red
            preferences[PreferencesKeys.DZUHUR_COLOR_G] = defaultDzuhurColor.green
            preferences[PreferencesKeys.DZUHUR_COLOR_B] = defaultDzuhurColor.blue

            preferences[PreferencesKeys.ASHAR_COLOR_R] = defaultAsharColor.red
            preferences[PreferencesKeys.ASHAR_COLOR_G] = defaultAsharColor.green
            preferences[PreferencesKeys.ASHAR_COLOR_B] = defaultAsharColor.blue

            preferences[PreferencesKeys.MAGHRIB_COLOR_R] = defaultMaghribColor.red
            preferences[PreferencesKeys.MAGHRIB_COLOR_G] = defaultMaghribColor.green
            preferences[PreferencesKeys.MAGHRIB_COLOR_B] = defaultMaghribColor.blue

            preferences[PreferencesKeys.ISYA_COLOR_R] = defaultIsyaColor.red
            preferences[PreferencesKeys.ISYA_COLOR_G] = defaultIsyaColor.green
            preferences[PreferencesKeys.ISYA_COLOR_B] = defaultIsyaColor.blue
        }
    }

    private fun createUserPreferences(preferences: Preferences): UserPreferences {
        return UserPreferences(
            locationRationaleHasBeenShow = preferences[PreferencesKeys.LOCATION_RATIONALE_HAS_BEEN_SHOW].orDefault(),
            currentRegencyCity = preferences[PreferencesKeys.CURRENT_REGENCY_CITY].orEmpty(),
            lastRegencyCity = preferences[PreferencesKeys.LAST_REGENCY_CITY].orEmpty(),
            subuhTime = preferences[PreferencesKeys.SUBUH_TIME].orEmpty(),
            subuhColorR = preferences[PreferencesKeys.SUBUH_COLOR_R].orDefault(
                0f,
                default = defaultSubuhColor.red
            ),
            subuhColorG = preferences[PreferencesKeys.SUBUH_COLOR_G].orDefault(
                0f,
                default = defaultSubuhColor.green
            ),
            subuhColorB = preferences[PreferencesKeys.SUBUH_COLOR_B].orDefault(
                0f,
                default = defaultSubuhColor.blue
            ),
            terbitTime = preferences[PreferencesKeys.TERBIT_TIME].orEmpty(),
            dzuhurTime = preferences[PreferencesKeys.DZUHUR_TIME].orEmpty(),
            dzuhurColorR = preferences[PreferencesKeys.DZUHUR_COLOR_R].orDefault(
                0f,
                default = defaultDzuhurColor.red
            ),
            dzuhurColorG = preferences[PreferencesKeys.DZUHUR_COLOR_G].orDefault(
                0f,
                default = defaultDzuhurColor.green
            ),
            dzuhurColorB = preferences[PreferencesKeys.DZUHUR_COLOR_B].orDefault(
                0f,
                default = defaultDzuhurColor.blue
            ),
            asharTime = preferences[PreferencesKeys.ASHAR_TIME].orEmpty(),
            asharColorR = preferences[PreferencesKeys.ASHAR_COLOR_R].orDefault(
                0f,
                default = defaultAsharColor.red
            ),
            asharColorG = preferences[PreferencesKeys.ASHAR_COLOR_G].orDefault(
                0f,
                default = defaultAsharColor.green
            ),
            asharColorB = preferences[PreferencesKeys.ASHAR_COLOR_B].orDefault(
                0f,
                default = defaultAsharColor.blue
            ),
            maghribTime = preferences[PreferencesKeys.MAGHRIB_TIME].orEmpty(),
            maghribColorR = preferences[PreferencesKeys.MAGHRIB_COLOR_R].orDefault(
                0f,
                default = defaultMaghribColor.red
            ),
            maghribColorG = preferences[PreferencesKeys.MAGHRIB_COLOR_G].orDefault(
                0f,
                default = defaultMaghribColor.green
            ),
            maghribColorB = preferences[PreferencesKeys.MAGHRIB_COLOR_B].orDefault(
                0f,
                default = defaultMaghribColor.blue
            ),
            isyaTime = preferences[PreferencesKeys.ISYA_TIME].orEmpty(),
            isyaColorR = preferences[PreferencesKeys.ISYA_COLOR_R].orDefault(
                0f,
                default = defaultIsyaColor.red
            ),
            isyaColorG = preferences[PreferencesKeys.ISYA_COLOR_G].orDefault(
                0f,
                default = defaultIsyaColor.green
            ),
            isyaColorB = preferences[PreferencesKeys.ISYA_COLOR_B].orDefault(
                0f,
                default = defaultIsyaColor.blue
            )
        )
    }

    override suspend fun getLastRegencyCity(): String? {
        return try {
            dataStore.data.first()[PreferencesKeys.LAST_REGENCY_CITY]
        } catch (e: Exception) {
            null
        }
    }
}