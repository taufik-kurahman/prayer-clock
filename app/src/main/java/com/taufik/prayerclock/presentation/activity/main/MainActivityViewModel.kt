package com.taufik.prayerclock.presentation.activity.main

import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taufik.prayerclock.application.util.getAddress
import com.taufik.prayerclock.data.preferences.PreferencesKeys
import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.data.repository.UserPreferencesRepository
import com.taufik.prayerclock.domain.model.PrayerScheduleModel
import com.taufik.prayerclock.domain.use_case.GetAvailableRegenciesCitiesUseCase
import com.taufik.prayerclock.domain.use_case.GetAvailableRegencyCityIdUseCase
import com.taufik.prayerclock.domain.use_case.GetMonthlySchedulesUseCase
import com.taufik.prayerclock.domain.use_case.InsertAllAvailableRegenciesCitiesUseCase
import com.taufik.prayerclock.domain.use_case.InsertAllPrayerScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val prayerScheduleRepository: PrayerScheduleRepository,
    private val getAvailableRegenciesCitiesUseCase: GetAvailableRegenciesCitiesUseCase,
    private val insertAllAvailableRegenciesCitiesUseCase: InsertAllAvailableRegenciesCitiesUseCase,
    private val getMonthlySchedulesUseCase: GetMonthlySchedulesUseCase,
    private val getAvailableRegencyCityIdUseCase: GetAvailableRegencyCityIdUseCase,
    private val insertAllPrayerScheduleUseCase: InsertAllPrayerScheduleUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MainActivityState())
    val state = _state.asStateFlow()

    init {
        userPreferencesRepository.userPreferences.onEach { userPreferences ->
            _state.value = _state.value.copy(
                locationRationaleHasBeenShow = userPreferences.locationRationaleHasBeenShow,
                currentRegencyCity = userPreferences.currentRegencyCity,
                lastRegencyCity = userPreferences.lastRegencyCity
            )
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val availableRegenciesCitiesCount = async {
                prayerScheduleRepository.getAvailableRegenciesCitiesCount()
            }

            if (availableRegenciesCitiesCount.await() == 0) {
                getAvailableRegenciesCitiesUseCase()
                    .onFailure {
                        addError(it)
                    }
                    .onSuccess {
                        withContext(Dispatchers.IO) {
                            insertAllAvailableRegenciesCitiesUseCase(it)
                        }
                    }
            }
        }
    }

    private fun addError(throwable: Throwable) {
        val newErrors = _state.value.errors.toMutableList()
        newErrors.add(Exception(throwable.localizedMessage))
        _state.value = _state.value.copy(
            errors = newErrors
        )
    }

    fun markRationaleHasBeenShow() {
        viewModelScope.launch {
            userPreferencesRepository.writeBoolean(
                PreferencesKeys.LOCATION_RATIONALE_HAS_BEEN_SHOW,
                true
            )
        }
    }

    fun getLocationAddress(geocoder: Geocoder, location: Location) {
        geocoder.getAddress(lat = location.latitude, lng = location.longitude) { address ->
            if (address != null) {
                var prefix = address.subAdminArea.split(" ").first()
                if (prefix.equals("kabupaten", true)) {
                    prefix = "Kab."
                }
                val regencyCity = "$prefix ${address.subAdminArea.split(" ").last()}"
                if (_state.value.currentRegencyCity != regencyCity) {
                    viewModelScope.launch {
                        userPreferencesRepository.writeString(
                            PreferencesKeys.CURRENT_REGENCY_CITY,
                            regencyCity
                        )
                    }
                }
            }
        }
    }

    fun getMonthlySchedules(currentRegencyCity: String) {
        if (currentRegencyCity != _state.value.lastRegencyCity) {
            viewModelScope.launch {
                val regencyCityId = async {
                    getAvailableRegencyCityIdUseCase(currentRegencyCity)
                }.await()

                if (regencyCityId != null) {
                    getMonthlySchedulesUseCase(regencyCityId)
                        .onFailure {
                            addError(it)
                        }
                        .onSuccess {
                            insertAllPrayerSchedule(currentRegencyCity, it)
                        }
                }
            }
        }
    }

    private fun insertAllPrayerSchedule(
        currentRegencyCity: String,
        prayerScheduleModel: PrayerScheduleModel
    ) {
        viewModelScope.launch {
            launch {
                insertAllPrayerScheduleUseCase(prayerScheduleModel.schedule)
            }.join()

            userPreferencesRepository.writeString(
                PreferencesKeys.LAST_REGENCY_CITY,
                currentRegencyCity
            )
        }
    }
}