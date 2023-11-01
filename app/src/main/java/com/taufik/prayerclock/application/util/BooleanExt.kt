package com.taufik.prayerclock.application.util

fun Boolean?.orDefault(default: Boolean = false): Boolean = this ?: default