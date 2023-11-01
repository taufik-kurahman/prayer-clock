package com.taufik.prayerclock.domain.use_case

import com.taufik.prayerclock.application.util.DateTimeUtil
import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.domain.mapper.toPrayerScheduleModel
import com.taufik.prayerclock.domain.model.PrayerScheduleModel
import javax.inject.Inject

class GetMonthlySchedulesUseCase @Inject constructor(
    private val prayerScheduleRepository: PrayerScheduleRepository
) {
    suspend operator fun invoke(
        regencyCityId: String,
    ): Result<PrayerScheduleModel> {
        return try {
            val result = prayerScheduleRepository.getMonthlySchedules(
                regencyCityId,
                DateTimeUtil.getCurrent("yyyy"),
                DateTimeUtil.getCurrent("MM")
            )

            Result.success(result.toPrayerScheduleModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}