package com.taufik.prayerclock.domain.use_case

import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import javax.inject.Inject

class GetAvailableRegencyCityIdUseCase @Inject constructor(
    private val prayerScheduleRepository: PrayerScheduleRepository
) {
    suspend operator fun invoke(regencyCityName: String): String? {
        return prayerScheduleRepository.getAvailableRegencyCityId(regencyCityName.uppercase())
    }
}