package com.taufik.prayerclock.domain.use_case

import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.domain.mapper.toAvailableRegencyCityModel
import com.taufik.prayerclock.domain.model.AvailableRegencyCityModel
import javax.inject.Inject

class GetAvailableRegenciesCitiesUseCase @Inject constructor(
    private val prayerScheduleRepository: PrayerScheduleRepository
) {
    suspend operator fun invoke(): Result<List<AvailableRegencyCityModel>> {
        return try {
            Result.success(
                prayerScheduleRepository.getAvailableRegenciesCities()
                    .map { it.toAvailableRegencyCityModel() }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}