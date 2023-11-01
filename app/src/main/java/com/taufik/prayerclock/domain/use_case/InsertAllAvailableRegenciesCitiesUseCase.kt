package com.taufik.prayerclock.domain.use_case

import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.domain.mapper.toAvailableRegencyCityEntity
import com.taufik.prayerclock.domain.model.AvailableRegencyCityModel
import javax.inject.Inject

class InsertAllAvailableRegenciesCitiesUseCase @Inject constructor(
    private val prayerScheduleRepository: PrayerScheduleRepository
) {
    suspend operator fun invoke(data: List<AvailableRegencyCityModel>) {
        prayerScheduleRepository.insertAvailableRegenciesCities(data.map { it.toAvailableRegencyCityEntity() })
    }
}