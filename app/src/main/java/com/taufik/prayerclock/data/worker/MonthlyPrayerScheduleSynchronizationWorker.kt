package com.taufik.prayerclock.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.taufik.prayerclock.application.util.DateTimeUtil
import com.taufik.prayerclock.application.util.getDateFormatted
import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.data.repository.UserPreferencesRepository
import com.taufik.prayerclock.domain.model.PrayerScheduleItemModel
import com.taufik.prayerclock.domain.use_case.GetAvailableRegencyCityIdUseCase
import com.taufik.prayerclock.domain.use_case.GetMonthlySchedulesUseCase
import com.taufik.prayerclock.domain.use_case.InsertAllPrayerScheduleUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class MonthlyPrayerScheduleSynchronizationWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val prayerScheduleRepository: PrayerScheduleRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val getAvailableRegencyCityIdUseCase: GetAvailableRegencyCityIdUseCase,
    private val getMonthlySchedulesUseCase: GetMonthlySchedulesUseCase,
    private val insertAllPrayerScheduleUseCase: InsertAllPrayerScheduleUseCase
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val NAME_TAG = "prayer_clock_monthly_prayer_schedule_synchronization"
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val workResult: Result = async {
                val lastRegencyCity = async {
                    userPreferencesRepository.getLastRegencyCity()
                }.await()

                if (!lastRegencyCity.isNullOrBlank()) {
                    val regencyCityId = async {
                        getAvailableRegencyCityIdUseCase(lastRegencyCity)
                    }.await()

                    if (regencyCityId != null) {
                        val savedDate = async {
                            prayerScheduleRepository.getDate()
                        }.await()

                        if (DateTimeUtil.getCurrent("MM") != savedDate.getDateFormatted(
                                inputFormat = PrayerScheduleItemModel.DATE_PATTERN,
                                outputFormat = "MM"
                            )
                        ) {
                            val monthlySchedulesUseCaseResult = async {
                                getMonthlySchedulesUseCase(regencyCityId)
                            }.await()

                            if (monthlySchedulesUseCaseResult.isFailure) {
                                Result.retry()
                            } else {
                                monthlySchedulesUseCaseResult.getOrNull()?.let {
                                    launch {
                                        insertAllPrayerScheduleUseCase(it.schedule)
                                    }.join()

                                    Result.success()
                                } ?: Result.retry()
                            }

                        } else {
                            Result.success()
                        }
                    } else {
                        Result.success()
                    }
                } else {
                    Result.success()
                }
            }.await()

            val repeatWorkRequest = OneTimeWorkRequestBuilder<MonthlyPrayerScheduleSynchronizationWorker>()
                .setInitialDelay(DateTimeUtil.getTimeDiffMillisOf(1), TimeUnit.MILLISECONDS)
                .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
                .addTag(NAME_TAG)
                .build()

            WorkManager.getInstance(appContext).enqueueUniqueWork(
                NAME_TAG,
                ExistingWorkPolicy.KEEP,
                repeatWorkRequest
            )

            workResult
        }
    }
}