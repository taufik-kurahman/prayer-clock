package com.taufik.prayerclock.domain.model

data class UserPreferences(
    val locationRationaleHasBeenShow: Boolean,
    val currentRegencyCity: String,
    val lastRegencyCity: String,
    val subuhTime: String,
    val subuhColorR: Float,
    val subuhColorG: Float,
    val subuhColorB: Float,
    val terbitTime: String,
    val dzuhurTime: String,
    val dzuhurColorR: Float,
    val dzuhurColorG: Float,
    val dzuhurColorB: Float,
    val asharTime: String,
    val asharColorR: Float,
    val asharColorG: Float,
    val asharColorB: Float,
    val maghribTime: String,
    val maghribColorR: Float,
    val maghribColorG: Float,
    val maghribColorB: Float,
    val isyaTime: String,
    val isyaColorR: Float,
    val isyaColorG: Float,
    val isyaColorB: Float
) {
    companion object {
        fun empty(): UserPreferences = UserPreferences(
            false,
            "",
            "",
            "",
            0f,
            0f,
            0f,
            "",
            "",
            0f,
            0f,
            0f,
            "",
            0f,
            0f,
            0f,
            "",
            0f,
            0f,
            0f,
            "",
            0f,
            0f,
            0f
        )
    }
}