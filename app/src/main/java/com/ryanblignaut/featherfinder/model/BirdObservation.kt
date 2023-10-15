package com.ryanblignaut.featherfinder.model

//bird species, location, date, and additional notes
class BirdObservation(
    birdSpecies: String,
    date: String,
    time: String,
    notes: String,
    lat: String,
    long: String
) {

    val birdObsTitle: BirdObsTitle = BirdObsTitle()
    val birdObsDetails: BirdObsDetails = BirdObsDetails()
    val birdObsLocation: BirdObsLocation = BirdObsLocation()

    init {
        this.birdObsTitle.birdSpecies = birdSpecies
        this.birdObsTitle.date = date
        this.birdObsDetails.time = time
        this.birdObsDetails.notes = notes
    }


}

class BirdObsDetails {
    var time: String = ""
    var notes: String = ""
}

class BirdObsTitle {
    var id: String = ""
    var birdSpecies: String = ""
    var date: String = ""
}

class BirdObsLocation {
    var lat: String = ""
    var long: String = ""
}