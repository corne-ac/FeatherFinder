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

    init {
        this.birdObsTitle.birdSpecies = birdSpecies
        this.birdObsTitle.date = date
        this.birdObsDetails.time = time
        this.birdObsDetails.notes = notes
        this.birdObsDetails.lat = lat
        this.birdObsDetails.long = long
    }


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