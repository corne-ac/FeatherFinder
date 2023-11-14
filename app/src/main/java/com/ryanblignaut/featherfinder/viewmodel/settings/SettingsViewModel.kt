package com.ryanblignaut.featherfinder.viewmodel.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.UserSettings
import com.ryanblignaut.featherfinder.utils.SettingReferences
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SettingsViewModel : BaseViewModel<UserSettings?>() {
    fun updateSettings(userSettings: UserSettings) = viewModelScope.launch(Dispatchers.IO) {
        FirestoreDataManager.saveSettings(userSettings)
    }

/*    private fun updateAllReferences(userSettings: UserSettings) {
        if (userSettings.isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        SettingReferences.userSettings = userSettings
    }*/

    fun getUserSettings() = fetchInBackground {
        FirestoreDataManager.getSettings()
    }


}