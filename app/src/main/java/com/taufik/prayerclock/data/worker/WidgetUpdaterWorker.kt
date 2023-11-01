package com.taufik.prayerclock.data.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.taufik.prayerclock.presentation.widget.PrayerClockWidget
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltWorker
class WidgetUpdaterWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val NAME_TAG = "prayer_clock_widget_updater"
    }

    override suspend fun doWork(): Result {
        return try {
            updateWidget().join()

            val repeatWorkRequest = OneTimeWorkRequestBuilder<WidgetUpdaterWorker>()
                .addTag(NAME_TAG)
                .build()

            WorkManager.getInstance(appContext).enqueueUniqueWork(
                NAME_TAG,
                ExistingWorkPolicy.REPLACE,
                repeatWorkRequest
            )

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private suspend fun updateWidget(): Job {
        return withContext(Dispatchers.IO) {
            launch {
                /**
                 * Update PrayerClockWidget every second for 5 minutes
                 */
                for (second in 1..300) {
                    delay(1000L)
                    PrayerClockWidget().updateAll(appContext)
                }
            }
        }
    }
}