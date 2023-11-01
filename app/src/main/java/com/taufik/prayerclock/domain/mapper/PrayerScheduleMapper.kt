package com.taufik.prayerclock.domain.mapper

import com.taufik.prayerclock.data.data_source.local.db.entity.PrayerScheduleEntity
import com.taufik.prayerclock.data.dto.PrayerScheduleItemResponseDto
import com.taufik.prayerclock.data.dto.PrayerScheduleResponseDto
import com.taufik.prayerclock.domain.model.PrayerScheduleItemModel
import com.taufik.prayerclock.domain.model.PrayerScheduleModel

fun PrayerScheduleResponseDto.toPrayerScheduleModel() = PrayerScheduleModel(
    schedule = schedule?.map { it.toPrayerScheduleItemModel() }.orEmpty()
)

fun PrayerScheduleItemResponseDto.toPrayerScheduleItemModel() = PrayerScheduleItemModel(
    tanggal = tanggal.orEmpty(),
    imsak = imsak.orEmpty(),
    subuh = subuh.orEmpty(),
    terbit = terbit.orEmpty(),
    dhuha = dhuha.orEmpty(),
    dzuhur = dzuhur.orEmpty(),
    ashar = ashar.orEmpty(),
    maghrib = maghrib.orEmpty(),
    isya = isya.orEmpty(),
    date = date.orEmpty()
)

fun PrayerScheduleItemModel.toPrayerScheduleEntity() = PrayerScheduleEntity(
    id = 0,
    tanggal = tanggal,
    imsak = imsak,
    subuh = subuh,
    terbit = terbit,
    dhuha = dhuha,
    dzuhur = dzuhur,
    ashar = ashar,
    maghrib = maghrib,
    isya = isya,
    date = date
)

fun PrayerScheduleEntity.toPrayerScheduleItemModel() = PrayerScheduleItemModel(
    tanggal = tanggal,
    imsak = imsak,
    subuh = subuh,
    terbit = terbit,
    dhuha = dhuha,
    dzuhur = dzuhur,
    ashar = ashar,
    maghrib = maghrib,
    isya = isya,
    date = date
)