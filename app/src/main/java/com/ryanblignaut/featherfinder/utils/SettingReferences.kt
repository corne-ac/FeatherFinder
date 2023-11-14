package com.ryanblignaut.featherfinder.utils

import com.ryanblignaut.featherfinder.model.UserSettings

object SettingReferences {
    enum class MaxDist(val maxDist: Float) {
        ONE(1f), FIVE(5f), TEN(10f), FIFTY(50f)
    }

    var userSettings = UserSettings(
        isMetric = true, isDarkMode = true, maxDistance = 1
    )

    fun getMaxDistance(): Float {
        // Here we will convert the max distance to the correct unit
        if (!userSettings.isMetric)
            MetricImperialConverter.kilometersToMiles(MaxDist.entries[userSettings.getMaxDist()].maxDist.toDouble())
        return MaxDist.entries[userSettings.getMaxDist()].maxDist
    }

}