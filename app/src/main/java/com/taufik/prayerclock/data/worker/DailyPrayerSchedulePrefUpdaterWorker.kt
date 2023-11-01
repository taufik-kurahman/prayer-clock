package com.taufik.prayerclock.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.taufik.prayerclock.application.util.DateTimeUtil
import com.taufik.prayerclock.data.preferences.PreferencesKeys
import com.taufik.prayerclock.data.repository.UserPreferencesRepository
import com.taufik.prayerclock.domain.use_case.GetOneShotTodayScheduleUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class DailyPrayerSchedulePrefUpdaterWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val getOneShotTodayScheduleUseCase: GetOneShotTodayScheduleUseCase
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val NAME_TAG = "prayer_clock_daily_prayer_schedule_pref_updater"
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val workResult: Result = async {
                val todaySchedule = async {
                    getOneShotTodayScheduleUseCase()
                }.await()

                if (todaySchedule != null) {
                    updatePrayerTimePref(
                        todaySchedule.subuh,
                        todaySchedule.terbit,
                        todaySchedule.dzuhur,
                        todaySchedule.ashar,
                        todaySchedule.maghrib,
                        todaySchedule.isya
                    ).join()

                    Result.success()
                } else {
                    Result.retry()
                }
            }.await()

            val repeatWorkRequest = OneTimeWorkRequestBuilder<DailyPrayerSchedulePrefUpdaterWorker>()
                .setInitialDelay(DateTimeUtil.getTimeDiffMillisOf(1), TimeUnit.MILLISECONDS)
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

    private suspend fun updatePrayerTimePref(
        subuhTime: String,
        terbitTime: String,
        dzuhurTime: String,
        asharTime: String,
        maghribTime: String,
        isyaTime: String
    ): Job {
        return withContext(Dispatchers.IO) {
            launch {
                userPreferencesRepository.writeString(
                    listOf(
                        Pair(PreferencesKeys.SUBUH_TIME, subuhTime),
                        Pair(PreferencesKeys.TERBIT_TIME, terbitTime),
                        Pair(PreferencesKeys.DZUHUR_TIME, dzuhurTime),
                        Pair(PreferencesKeys.ASHAR_TIME, asharTime),
                        Pair(PreferencesKeys.MAGHRIB_TIME, maghribTime),
                        Pair(PreferencesKeys.ISYA_TIME, isyaTime)
                    )
                )
            }
        }
    }
}