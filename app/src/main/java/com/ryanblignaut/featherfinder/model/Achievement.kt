package com.ryanblignaut.featherfinder.model

class Achievement(
    val id: String,
    val title: String,
    val description: String,
    var completed: Boolean? = null,
)
