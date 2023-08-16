package com.ryanblignaut.featherfinder.api

import com.ryanblignaut.featherfinder.utils.ApiRequestUtility


//Docs https://openrouteservice.org/dev/#/api-docs/v2/directions/{profile}/json/post

enum class VehicleType(val type: String) {
    CAR("driving-car"), CAR_HGV("driving-hgv"), BIKE("cycling-regular"), BIKE_ELECTRIC("cycling-electric"), BIKE_MOUNTAIN(
        "cycling-mountain"
    ),
    BIKE_ROAD("cycling-road"), WALK("foot-walking"), WALK_HIKING("foot-hiking"), WHEELCHAIR("wheelchair")
}

class OpenRoutesApi {

    //    private val
    private val vType: VehicleType = VehicleType.BIKE_ROAD
    private val baseUrl = "https://api.openrouteservice.org/v2/directions/"
    private val version = "/v2"
    private val tokenKey = "5b3ce3597851110001cf624831b0c31c47624979bcb76b5d4e26d30c"

    // Geo JSON map
    private val geoJsonHeaders = mapOf(
        "Authorization" to tokenKey,
        "Content-Type" to "application/json; charset=utf-8",
        "Accept" to "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8"
    )

    fun fetchRoute(
        startLat: Double, startLon: Double, endLon: Double, endLat: Double, distance: Int
    ): Result<String> {
        val start = "$startLon,$startLat"
        val end = "$endLon,$endLat"

        /*
            Example co-ords for testing
            val start = "28.14248,-25.874163"
            val end = "28.16059,-25.890457"
        */

        println(start)
        println(end)
        val url = "$baseUrl${vType.type}?api_key=$tokenKey&start=$end&end=$start"

        return ApiRequestUtility.makeRequestString(url, headers = geoJsonHeaders)
//        return ApiRequestUtility.makeRequestJson<String>(
//            url,
//            headers = geoJsonHeaders,
//        )

    }


}