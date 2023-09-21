package com.ryanblignaut.featherfinder.model

//bird species, location, date, and additional notes
class BirdObservation(
    birdSpecies: String,
    date: String,
    time: String,
    location: String,
    notes: String,
) {

    val birdObsTitle: BirdObsTitle = BirdObsTitle()
    val birdObsDetails: BirdObsDetails = BirdObsDetails()

    init {
        this.birdObsTitle.birdSpecies = birdSpecies
        this.birdObsTitle.date = date
        this.birdObsDetails.time = time
        this.birdObsDetails.location = location
        this.birdObsDetails.notes = notes
    }


}

class BirdObsDetails {
    var time: String = ""
    var location: String = ""
    var notes: String = ""
}

class BirdObsTitle {
    var id: String = ""
    var birdSpecies: String = ""
    var date: String = ""
}
