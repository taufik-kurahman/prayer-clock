package com.taufik.prayerclock.application.util

import android.util.Log
import com.taufik.prayerclock.BuildConfig

object Logger {
    private val logEnabled = !BuildConfig.BUILD_TYPE.equals("release", true)

    fun info(tag: String, message: String, tr: Throwable? = null) {
        if (logEnabled) {
            Log.i(tag, message, tr)
        }
    }

    fun error(tag: String, message: String, tr: Throwable? = null) {
        if (logEnabled) {
            Log.e(tag, message, tr)
        }
    }
}