package com.taufik.prayerclock.domain.use_case

import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.domain.mapper.toPrayerScheduleEntity
import com.taufik.prayerclock.domain.model.PrayerScheduleItemModel
import javax.inject.Inject

class InsertAllPrayerScheduleUseCase @Inject constructor(
    private val prayerScheduleRepository: PrayerScheduleRepository
) {
    suspend operator fun invoke(data: List<PrayerScheduleItemModel>) {
        prayerScheduleRepository.insertAllPrayerSchedule(data.map { it.toPrayerScheduleEntity() })
    }
}