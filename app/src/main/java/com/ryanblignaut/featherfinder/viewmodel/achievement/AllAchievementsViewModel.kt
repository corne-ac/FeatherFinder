package com.ryanblignaut.featherfinder.viewmodel.achievement

import com.ryanblignaut.featherfinder.firebase.FirebaseDataManager
import com.ryanblignaut.featherfinder.model.Achievement
import com.ryanblignaut.featherfinder.viewmodel.helper.BaseViewModel

class AllAchievementsViewModel : BaseViewModel<List<Achievement>>() {
    fun getAchievements() = fetchInBackground {
        val allAch = FirebaseDataManager.getAllAchievements()
        val userAch = FirebaseDataManager.getUserAchievements()
        userAch.forEach { ach ->
            if (allAch.containsKey(ach.id)) {
                allAch[ach.id]?.completed = true
            }
        }
        Result.success(allAch.values.toList())
    }
}
