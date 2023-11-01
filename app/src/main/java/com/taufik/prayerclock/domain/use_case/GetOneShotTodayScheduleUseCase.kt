package com.taufik.prayerclock.domain.use_case

import com.taufik.prayerclock.application.util.DateTimeUtil
import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.domain.mapper.toPrayerScheduleItemModel
import com.taufik.prayerclock.domain.model.PrayerScheduleItemModel
import javax.inject.Inject

class GetOneShotTodayScheduleUseCase @Inject constructor(
    private val prayerScheduleRepository: PrayerScheduleRepository
) {
    suspend operator fun invoke(): PrayerScheduleItemModel? {
        return prayerScheduleRepository.getOneShotPrayerScheduleByDate(
            DateTimeUtil.getCurrent(
                PrayerScheduleItemModel.DATE_PATTERN
            )
        )?.toPrayerScheduleItemModel()
    }
}