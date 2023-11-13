package com.ryanblignaut.featherfinder.model

class Goal(name: String, startTime: String, endTime: String, description: String) {
    val goalTitle: GoalTitle = GoalTitle()
    val goalDetail: GoalDetail = GoalDetail()

    init {
        this.goalTitle.name = name
        this.goalTitle.description = description
        this.goalDetail.startTime = startTime
        this.goalDetail.endTime = endTime
    }

}

class GoalTitle {
    var id: String = ""
    var name: String = ""
    var description: String = ""
}

class GoalDetail {
    var startTime: String = ""
    var endTime: String = ""
}

class FullGoal(
    var id: String = "",
    var name: String = "",
    var description: String = "",
    var startTime: Long = -1,
    var endTime: Long = -1,
) : SelfId()

