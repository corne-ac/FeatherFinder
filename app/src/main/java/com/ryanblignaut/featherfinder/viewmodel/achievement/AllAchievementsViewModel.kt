package com.ryanblignaut.featherfinder.viewmodel.achievement

import com.ryanblignaut.featherfinder.firebase.FirestoreDataManager
import com.ryanblignaut.featherfinder.model.UserAchievement
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class AllAchievementsViewModel : BaseViewModel<UserAchievement?>() {
    fun getAchievements() = fetchInBackground {
        FirestoreDataManager.requestUserAchievements()
    }
}
