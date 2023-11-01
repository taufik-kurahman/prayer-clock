package com.taufik.prayerclock.presentation.screen.main

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.prayerclock.data.preferences.PreferencesKeys
import com.taufik.prayerclock.data.repository.UserPreferencesRepository
import com.taufik.prayerclock.domain.model.PrayerTime
import com.taufik.prayerclock.domain.model.UserPreferences
import com.taufik.prayerclock.domain.use_case.GetTodayScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    getTodayScheduleUseCase: GetTodayScheduleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    init {
        getCurrentTimeMillis()
        userPreferencesRepository.userPreferences.onEach { userPreferences ->
            loadColors(userPreferences)

            updatePrayerTimeState(userPreferences)
        }.launchIn(viewModelScope)

        getTodayScheduleUseCase().onEach { prayerScheduleItemModel ->
            if (prayerScheduleItemModel != null) {
                updatePrayerTimePref(
                    prayerScheduleItemModel.subuh,
                    prayerScheduleItemModel.terbit,
                    prayerScheduleItemModel.dzuhur,
                    prayerScheduleItemModel.ashar,
                    prayerScheduleItemModel.maghrib,
                    prayerScheduleItemModel.isya
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun updatePrayerTimePref(
        subuhTime: String,
        terbitTime: String,
        dzuhurTime: String,
        asharTime: String,
        maghribTime: String,
        isyaTime: String
    ) {
        viewModelScope.launch {
            userPreferencesRepository.writeString(listOf(
                Pair(PreferencesKeys.SUBUH_TIME, subuhTime),
                Pair(PreferencesKeys.TERBIT_TIME, terbitTime),
                Pair(PreferencesKeys.DZUHUR_TIME, dzuhurTime),
                Pair(PreferencesKeys.ASHAR_TIME, asharTime),
                Pair(PreferencesKeys.MAGHRIB_TIME, maghribTime),
                Pair(PreferencesKeys.ISYA_TIME, isyaTime)
            ))
        }
    }

    private fun getCurrentTimeMillis() {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                _state.value = _state.value.copy(currentTimeMillis = System.currentTimeMillis())
            }
        }
    }

    private fun loadColors(userPreferences: UserPreferences) {
        _state.value = _state.value.copy(
            subuhColor = Color(
                userPreferences.subuhColorR,
                userPreferences.subuhColorG,
                userPreferences.subuhColorB,
            ),
            dzuhurColor = Color(
                userPreferences.dzuhurColorR,
                userPreferences.dzuhurColorG,
                userPreferences.dzuhurColorB,
            ),
            asharColor = Color(
                userPreferences.asharColorR,
                userPreferences.asharColorG,
                userPreferences.asharColorB,
            ),
            maghribColor = Color(
                userPreferences.maghribColorR,
                userPreferences.maghribColorG,
                userPreferences.maghribColorB,
            ),
            isyaColor = Color(
                userPreferences.isyaColorR,
                userPreferences.isyaColorG,
                userPreferences.isyaColorB,
            )
        )
    }

    fun updateColor(prayerTime: PrayerTime, color: Color) {
        if (color.red == 0f && color.green == 0f && color.blue == 0f) return

        viewModelScope.launch {
            when (prayerTime) {
                PrayerTime.SUBUH -> {
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.SUBUH_COLOR_R,
                        color.red
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.SUBUH_COLOR_G,
                        color.green
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.SUBUH_COLOR_B,
                        color.blue
                    )
                }

                PrayerTime.DZUHUR -> {
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.DZUHUR_COLOR_R,
                        color.red
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.DZUHUR_COLOR_G,
                        color.green
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.DZUHUR_COLOR_B,
                        color.blue
                    )
                }

                PrayerTime.ASHAR -> {
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.ASHAR_COLOR_R,
                        color.red
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.ASHAR_COLOR_G,
                        color.green
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.ASHAR_COLOR_B,
                        color.blue
                    )
                }

                PrayerTime.MAGHRIB -> {
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.MAGHRIB_COLOR_R,
                        color.red
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.MAGHRIB_COLOR_G,
                        color.green
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.MAGHRIB_COLOR_B,
                        color.blue
                    )
                }

                PrayerTime.ISYA -> {
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.ISYA_COLOR_R,
                        color.red
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.ISYA_COLOR_G,
                        color.green
                    )
                    userPreferencesRepository.writeFloat(
                        PreferencesKeys.ISYA_COLOR_B,
                        color.blue
                    )
                }

                else -> Unit
            }
        }
    }

    fun resetColor() {
        viewModelScope.launch {
            userPreferencesRepository.resetColors()
        }
    }

    private fun updatePrayerTimeState(userPreferences: UserPreferences) {
        if (userPreferences.subuhTime.isBlank()
            && userPreferences.terbitTime.isBlank()
            && userPreferences.dzuhurTime.isBlank()
            && userPreferences.asharTime.isBlank()
            && userPreferences.maghribTime.isBlank()
            && userPreferences.isyaTime.isBlank()
        ) {
            return
        }

        _state.value = _state.value.copy(
            prayerTimes = listOf(
                Pair(PrayerTime.SUBUH, userPreferences.subuhTime),
                Pair(PrayerTime.TERBIT, userPreferences.terbitTime),
                Pair(PrayerTime.DZUHUR, userPreferences.dzuhurTime),
                Pair(PrayerTime.ASHAR, userPreferences.asharTime),
                Pair(PrayerTime.MAGHRIB, userPreferences.maghribTime),
                Pair(PrayerTime.ISYA, userPreferences.isyaTime)
            )
        )
    }
}