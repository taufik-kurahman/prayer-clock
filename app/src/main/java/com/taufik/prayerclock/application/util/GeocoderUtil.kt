package com.taufik.prayerclock.application.util

import android.content.Context
import android.location.Geocoder
import java.util.Locale

object GeocoderUtil {
    fun getDefaultGeoCoder(
        context: Context,
        locale: Locale = Locale("id", "ID")
    ): Geocoder {
        return Geocoder(context, locale)
    }
}