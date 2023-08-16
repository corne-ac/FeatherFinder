package com.ryanblignaut.featherfinder.utils

object MetricImperialConverter {
    // Conversion factors
    private const val KILOMETERS_TO_MILES_FACTOR = 0.621371
    private const val MILES_TO_KILOMETERS_FACTOR = 1.60934

    // Convert kilometers to miles
    fun kilometersToMiles(kilometers: Double): Double {
        return kilometers * KILOMETERS_TO_MILES_FACTOR
    }

    // Convert miles to kilometers
    fun milesToKilometers(miles: Double): Double {
        return miles * MILES_TO_KILOMETERS_FACTOR
    }
}