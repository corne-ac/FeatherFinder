package com.ryanblignaut.featherfinder.utils

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.widget.TextView

object LocationConverter {
/*
    fun getLocationFromLatLong(c: Context, lat: Double, lon: Double): String {

    }
*/


    // The geocoder.getFromLocation is deprecated in API 33 but we can target API 24 so we need to account both cases.
    // Suppress the deprecation warning for api values < 33.
    @Suppress("DEPRECATION")
    fun populateGeneralLocation(
        geocoder: Geocoder,
        lat: Double,
        lon: Double,
        tv: TextView,
    ) {
        // Docs for Geocoder reference the deprecated feature as well:  https://developer.android.com/reference/android/location/Geocoder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                lat,
                lon,
                1
            ) {
                tv.text = it.firstOrNull()?.getAddressLine(0)
            }
        } else {
            val addresses = geocoder.getFromLocation(
                lat,
                lon,
                1
            )
            if (!addresses.isNullOrEmpty()) {
                tv.text = addresses.firstOrNull()?.getAddressLine(0)
            }
        }

    }

}