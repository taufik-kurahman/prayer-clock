package com.taufik.prayerclock.domain.use_case

import com.taufik.prayerclock.application.util.DateTimeUtil
import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.domain.mapper.toPrayerScheduleItemModel
import com.taufik.prayerclock.domain.model.PrayerScheduleItemModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTodayScheduleUseCase @Inject constructor(
    private val prayerScheduleRepository: PrayerScheduleRepository
) {
    operator fun invoke(): Flow<PrayerScheduleItemModel?> {
        return prayerScheduleRepository.getPrayerScheduleByDate(
            DateTimeUtil.getCurrent(
                PrayerScheduleItemModel.DATE_PATTERN
            )
        ).map { it?.toPrayerScheduleItemModel() }
    }
}