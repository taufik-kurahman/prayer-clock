package com.taufik.prayerclock.presentation.activity.main

data class MainActivityState(
    val locationRationaleHasBeenShow: Boolean = false,
    val currentRegencyCity: String = "",
    val lastRegencyCity: String = "",
    val errors: List<Exception> = emptyList()
)