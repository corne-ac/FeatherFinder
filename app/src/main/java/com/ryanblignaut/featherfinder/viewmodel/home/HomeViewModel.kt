package com.ryanblignaut.featherfinder.viewmodel.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.FullGoal
import com.ryanblignaut.featherfinder.model.Goal
import com.ryanblignaut.featherfinder.model.UserAchievement
import com.ryanblignaut.featherfinder.model.UserSettings
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class HomeViewModel : BaseViewModel<UserSettings?>() {
    fun getUserSettings() = fetchInBackground {
        FirestoreDataManager.getSettings()
    }

    private val _mutAchievement = MutableLiveData<Result<UserAchievement?>>()
    val liveAchievement: LiveData<Result<UserAchievement?>> = _mutAchievement

    fun updateLoginStatus() = runAsync {
        _mutAchievement.postValue(FirestoreDataManager.requestUserAchievements())
    }

    fun updateLoginDays(loginDaysStreak: Int) = runAsync {
        FirestoreDataManager.updateDayStreak(loginDaysStreak)
    }


    // Really should have created a utility for this...
    private val _mutUrgentGoal = MutableLiveData<Result<FullGoal?>>()
    val urgentGoal: LiveData<Result<FullGoal?>> = _mutUrgentGoal

    fun updateUrgentGoal() = runAsync {
        _mutUrgentGoal.postValue(FirestoreDataManager.requestUrgentGoal())
    }


    /*    fun updateLoginStatus() = runAsync
        {
            FirestoreDataManager.updateLoginStatus(isLoggedIn)
        }*/


}