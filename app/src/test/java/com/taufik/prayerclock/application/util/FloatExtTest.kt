package com.taufik.prayerclock.application.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FloatExtTest {
    private var target: Float? = null

    @Test
    fun orDefaultTestOnNullTarget() {
        val expected = 0f
        assertEquals(expected, target.orDefault(expected))
    }

    @Test
    fun orDefaultTestWithNullOrOptionalTargetValue() {
        target = 0f
        val expected = 1f
        assertEquals(expected, target.orDefault(0f, default = expected))
    }
}