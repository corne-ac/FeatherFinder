package com.ryanblignaut.featherfinder.model

//bird species, location, date, and additional notes
data class BirdObservation(
    val id: String,
    val birdSpecies: String,
    val location: String,
    val date: String,
    val notes: String
)
