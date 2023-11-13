package com.ryanblignaut.featherfinder.model

/*
class Achievement(
    val id: String,
    val title: String,
    val description: String,
    var completed: Boolean? = null,
)
*/

class UserAchievement(
    val lastLoginTime: Long,
    val loginDaysStreak: Int,
    val totalBirdsSeen: Int,
    val totalGoalsComp: Int,
) {

}

data class Achievement(
    val name: String,
    val description: String,
    var isUnlocked: Boolean = false,
)

// This is a super crude implementation of the achievement system, expecting that the data will always be in increments of 1, 10 and 100.
object AchievementManager {

    private val achievements = mutableListOf<Achievement>()

    init {
        // Create a temp list of achievements.
        val initialAchievements = listOf(
            "First Bird" to "You saw your first bird!",
            "Bird Watcher" to "You saw 10 birds!",
            "Bird Master" to "You saw 100 birds!",
            "Goal Achiever" to "You completed your first goal!",
            "Goal Master" to "You completed 10 goals!",
            "Goal Expert" to "You completed 100 goals!",
            "Streak Starter" to "You logged in for the first time!",
            "Streak Master" to "You logged in for 10 days in a row!",
            "Streak Expert" to "You logged in for 100 days in a row!"
        )
        // Update our real list.
        initialAchievements.forEach { (name, description) ->
            achievements.add(Achievement(name, description))
        }
    }

    fun getUsersAchievements(ua: UserAchievement?): List<Achievement> {
        if (ua == null) {
            return achievements
        }

        val unlockingConditions = listOf(
            Pair(ua.totalBirdsSeen, listOf(0, 1, 2)),
            Pair(ua.totalGoalsComp, listOf(3, 4, 5)),
            Pair(ua.loginDaysStreak, listOf(6, 7, 8))
        )

        for ((value, indices) in unlockingConditions) {
            indices.forEach { index ->
                if (value > index * 9) {
                    achievements[index].isUnlocked = true
                }
            }
        }

        return achievements
    }
}



