package com.taufik.prayerclock.presentation.screen.main

import androidx.compose.ui.graphics.Color
import com.taufik.prayerclock.domain.model.PrayerTime
import com.taufik.prayerclock.presentation.theme.defaultAsharColor
import com.taufik.prayerclock.presentation.theme.defaultDzuhurColor
import com.taufik.prayerclock.presentation.theme.defaultIsyaColor
import com.taufik.prayerclock.presentation.theme.defaultMaghribColor
import com.taufik.prayerclock.presentation.theme.defaultSubuhColor

data class MainScreenState(
    val currentTimeMillis: Long = 0L,
    val prayerTimes: List<Pair<PrayerTime, String>> = listOf(),
    val subuhColor: Color = defaultSubuhColor,
    val dzuhurColor: Color = defaultDzuhurColor,
    val asharColor: Color = defaultAsharColor,
    val maghribColor: Color = defaultMaghribColor,
    val isyaColor: Color = defaultIsyaColor,
    val errors: List<Exception> = emptyList()
)
