package com.taufik.prayerclock.application.util

import android.location.Address
import android.location.Geocoder
import android.os.Build

@Suppress("DEPRECATION")
fun Geocoder.getAddress(
    lat: Double,
    lng: Double,
    maxResults: Int = 1,
    address: (Address?) -> Unit
) {
    /**
     * API level 33 and above
     */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getFromLocation(lat, lng, maxResults) { address(it.firstOrNull()) }
        return
    }

    try {
        address(getFromLocation(lat, lng, 1)?.firstOrNull())
    } catch (e: Exception) {
        address(null)
    }
}