package com.ryanblignaut.featherfinder.utils

import com.ryanblignaut.featherfinder.model.UserSettings

object SettingReferences {
    var userSettings = UserSettings(
        isMetric = true,
        isDarkMode = true,
        maxDistance = 1
    )
}