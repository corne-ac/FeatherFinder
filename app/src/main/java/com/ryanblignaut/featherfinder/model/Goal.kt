package com.ryanblignaut.featherfinder.model

import java.util.UUID

class Goal {
    var id: String = UUID.randomUUID().toString();
    var name: String = "";
    var startTime: String = "";
    var endTime: String = "";
    var description: String = "";

    constructor()
    constructor(
        name: String,
        startTime: String,
        endTime: String,
        description: String,
    ) {
        this.name =  name
        this.startTime =  startTime
        this.endTime =  endTime
        this.description =  description
    }

}
