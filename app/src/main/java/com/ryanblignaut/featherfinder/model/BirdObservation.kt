package com.ryanblignaut.featherfinder.model

import java.util.UUID

//bird species, location, date, and additional notes
class BirdObservation {
    var id: String = UUID.randomUUID().toString()
    var birdSpecies: String = ""
    var date: String = ""
    var time: String = ""
    var location: String = ""
    var notes: String = ""


    constructor()
    constructor(birdSpecies: String, date: String, time: String, location: String, notes: String) {
        this.birdSpecies = birdSpecies
        this.date = date
        this.time = time
        this.location = location
        this.notes = notes
    }
}