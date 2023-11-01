package com.taufik.prayerclock.application.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ClockUtilTest {
    @Test
    fun calculateAngleForHourTest() {
        val hours = 3
        val minutes = 0
        val expected = 90f
        assertEquals(expected, ClockUtil.calculateAngleForHour(hours, minutes))
    }

    @Test
    fun calculateAngleForMinuteTest() {
        val minutes = 30
        val seconds = 0
        val expected = 180f
        assertEquals(expected, ClockUtil.calculateAngleForMinute(minutes, seconds))
    }

    @Test
    fun calculateAngleForSecondTest() {
        val seconds = 15
        val expected = 90f
        assertEquals(expected, ClockUtil.calculateAngleForSecond(seconds))
    }
}