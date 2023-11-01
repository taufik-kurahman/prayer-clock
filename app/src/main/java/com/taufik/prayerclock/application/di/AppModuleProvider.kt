package com.taufik.prayerclock.application.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.taufik.prayerclock.BuildConfig
import com.taufik.prayerclock.application.constant.GlobalConfig
import com.taufik.prayerclock.application.constant.GlobalConfig.USER_PREFERENCES_NAME
import com.taufik.prayerclock.data.MyQuranService
import com.taufik.prayerclock.data.data_source.local.db.PrayerClockDatabase
import com.taufik.prayerclock.data.data_source.local.db.dao.AvailableRegencyCityDao
import com.taufik.prayerclock.data.data_source.local.db.dao.PrayerScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModuleProvider {
    private fun getLoggingLevel(): HttpLoggingInterceptor.Level {
        return if (BuildConfig.BUILD_TYPE.equals("release", true)) {
            HttpLoggingInterceptor.Level.NONE
        } else {
            HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, USER_PREFERENCES_NAME)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(USER_PREFERENCES_NAME) }
        )
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(getLoggingLevel()))
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MY_QURAN_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideMyQuranService(retrofit: Retrofit): MyQuranService {
        return retrofit.create(MyQuranService::class.java)
    }

    @Singleton
    @Provides
    fun providePrayerClockDatabase(@ApplicationContext appContext: Context): PrayerClockDatabase {
        return Room.databaseBuilder(
            appContext,
            PrayerClockDatabase::class.java,
            GlobalConfig.LOCAL_DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideAvailableRegencyCityDao(db: PrayerClockDatabase): AvailableRegencyCityDao {
        return db.availableRegencyCityDao()
    }

    @Singleton
    @Provides
    fun providePrayerScheduleDao(db: PrayerClockDatabase): PrayerScheduleDao {
        return db.prayerScheduleDao()
    }
}