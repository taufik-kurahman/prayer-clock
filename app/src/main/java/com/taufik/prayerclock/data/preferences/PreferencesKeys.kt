package com.taufik.prayerclock.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val LOCATION_RATIONALE_HAS_BEEN_SHOW = booleanPreferencesKey("location_rationale_has_been_show")
    val CURRENT_REGENCY_CITY = stringPreferencesKey("current_regency_city")
    val LAST_REGENCY_CITY = stringPreferencesKey("last_regency_city")
    val SUBUH_TIME = stringPreferencesKey("subuh_time")
    val SUBUH_COLOR_R = floatPreferencesKey("subuh_color_r")
    val SUBUH_COLOR_G = floatPreferencesKey("subuh_color_g")
    val SUBUH_COLOR_B = floatPreferencesKey("subuh_color_b")
    val TERBIT_TIME = stringPreferencesKey("terbit_time")
    val DZUHUR_TIME = stringPreferencesKey("dzuhur_time")
    val DZUHUR_COLOR_R = floatPreferencesKey("dzuhur_color_r")
    val DZUHUR_COLOR_G = floatPreferencesKey("dzuhur_color_g")
    val DZUHUR_COLOR_B = floatPreferencesKey("dzuhur_color_b")
    val ASHAR_TIME = stringPreferencesKey("ashar_time")
    val ASHAR_COLOR_R = floatPreferencesKey("ashar_color_r")
    val ASHAR_COLOR_G = floatPreferencesKey("ashar_color_g")
    val ASHAR_COLOR_B = floatPreferencesKey("ashar_color_b")
    val MAGHRIB_TIME = stringPreferencesKey("maghrib_time")
    val MAGHRIB_COLOR_R = floatPreferencesKey("maghrib_color_r")
    val MAGHRIB_COLOR_G = floatPreferencesKey("maghrib_color_g")
    val MAGHRIB_COLOR_B = floatPreferencesKey("maghrib_color_b")
    val ISYA_TIME = stringPreferencesKey("isya_time")
    val ISYA_COLOR_R = floatPreferencesKey("isya_color_r")
    val ISYA_COLOR_G = floatPreferencesKey("isya_color_g")
    val ISYA_COLOR_B = floatPreferencesKey("isya_color_b")
}