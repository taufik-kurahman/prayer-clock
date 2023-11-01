package com.taufik.prayerclock.data

import com.taufik.prayerclock.data.dto.AvailableRegencyCityResponseDto
import com.taufik.prayerclock.data.dto.PrayerScheduleResponseDto
import com.taufik.prayerclock.data.dto.ResponseWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MyQuranService {
    @GET("v1/sholat/kota/semua")
    suspend fun getAvailableRegenciesCities(): Response<List<AvailableRegencyCityResponseDto>>

    @GET("v1/sholat/jadwal/{regencyCityId}/{year}/{month}")
    suspend fun getMonthlySchedules(
        @Path("regencyCityId") regencyCityId: String,
        @Path("year") year: String,
        @Path("month") month: String
    ): Response<ResponseWrapper<PrayerScheduleResponseDto>>
}