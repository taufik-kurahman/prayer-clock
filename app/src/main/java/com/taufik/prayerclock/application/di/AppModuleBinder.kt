package com.taufik.prayerclock.application.di

import com.taufik.prayerclock.data.data_source.local.PrayerScheduleLocalDataSource
import com.taufik.prayerclock.data.data_source.local.impl.PrayerScheduleLocalDataSourceImpl
import com.taufik.prayerclock.data.data_source.remote.PrayerScheduleRemoteDataSource
import com.taufik.prayerclock.data.data_source.remote.impl.PrayerScheduleRemoteDataSourceImpl
import com.taufik.prayerclock.data.repository.PrayerScheduleRepository
import com.taufik.prayerclock.data.repository.UserPreferencesRepository
import com.taufik.prayerclock.data.repository.impl.PrayerScheduleRepositoryImpl
import com.taufik.prayerclock.data.repository.impl.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBinder {
    @Singleton
    @Binds
    abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository

    @Singleton
    @Binds
    abstract fun bindPrayerScheduleRepository(impl: PrayerScheduleRepositoryImpl): PrayerScheduleRepository

    @Singleton
    @Binds
    abstract fun bindPrayerScheduleRemoteDataSource(impl: PrayerScheduleRemoteDataSourceImpl): PrayerScheduleRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindPrayerScheduleLocalDataSource(impl: PrayerScheduleLocalDataSourceImpl): PrayerScheduleLocalDataSource
}