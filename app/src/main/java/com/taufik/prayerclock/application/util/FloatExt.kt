package com.taufik.prayerclock.application.util

fun Float?.orDefault(default: Float = 0f): Float = this ?: default

fun Float?.orDefault(vararg others: Float, default: Float = 0f): Float {
    val x = others.toList()
    return if (this == null || this in x) {
        default
    } else {
        this
    }
}