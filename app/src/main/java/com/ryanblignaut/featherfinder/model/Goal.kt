package com.ryanblignaut.featherfinder.model

import java.util.UUID

data class Goal(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val startTime: String,
    val endTime: String,
    val description: String,
)
