package com.taufik.prayerclock.data.data_source.remote.impl

import android.content.Context
import com.taufik.prayerclock.application.util.HttpResponseHandler
import com.taufik.prayerclock.data.MyQuranService
import com.taufik.prayerclock.data.data_source.remote.PrayerScheduleRemoteDataSource
import com.taufik.prayerclock.data.dto.AvailableRegencyCityResponseDto
import com.taufik.prayerclock.data.dto.PrayerScheduleResponseDto
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PrayerScheduleRemoteDataSourceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val myQuranService: MyQuranService
) : HttpResponseHandler(appContext), PrayerScheduleRemoteDataSource {
    override suspend fun getAvailableRegenciesCities(): List<AvailableRegencyCityResponseDto> {
        return myQuranService.getAvailableRegenciesCities().handleDirectResponse()
    }

    override suspend fun getMonthlySchedules(
        regencyCityId: String,
        year: String,
        month: String
    ): PrayerScheduleResponseDto {
        return myQuranService.getMonthlySchedules(
            regencyCityId,
            year,
            month
        ).handleWrappedResponse()
    }
}