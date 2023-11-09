package com.ryanblignaut.featherfinder.model

//bird species, location, date, and additional notes
class BirdObservation(
    private var birdSpecies: String = "",
    private var date: String = "",
    private var time: String = "",
    private var notes: String = "",
    private var lat: String = "",
    private var long: String = ""
) {

    val birdObsTitle: BirdObsTitle = BirdObsTitle()
    val birdObsDetails: BirdObsDetails = BirdObsDetails()

    init {
        this.birdObsTitle.birdSpecies = birdSpecies
        this.birdObsTitle.date = date
        this.birdObsDetails.time = time
        this.birdObsDetails.notes = notes
        this.birdObsDetails.lat = lat
        this.birdObsDetails.long = long
    }

//    constructor() : this("", "", "", "", "", "") {}

}

class BirdObsDetails {
    var time: String = ""
    var notes: String = ""
    var lat: String = ""
    var long: String = ""
}

class BirdObsTitle {
    var id: String = ""
    var birdSpecies: String = ""
    var date: String = ""
}

class FullBirdObservation(
    var birdSpecies: String = "",
    var date: String = "",
    var time: String = "",
    var notes: String = "",
    var lat: String = "",
    var long: String = ""
)