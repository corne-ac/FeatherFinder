package com.ryanblignaut.featherfinder.model

class UserSettings{
    var isDarkMode: Boolean = false;
    var isMetric: Boolean = false;
    var maxDistance: Int = 1;

    constructor(isDarkMode: Boolean, isMetric: Boolean, maxDistance: Int) {
        this.isDarkMode = isDarkMode
        this.isMetric = isMetric
        this.maxDistance = maxDistance
    }
    constructor()

}