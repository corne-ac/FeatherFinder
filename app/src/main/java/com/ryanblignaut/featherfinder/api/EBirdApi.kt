package com.ryanblignaut.featherfinder.api

import android.net.Uri
import com.ryanblignaut.featherfinder.model.api.EBirdHotspotData
import com.ryanblignaut.featherfinder.model.api.EBirdLocation
import com.ryanblignaut.featherfinder.utils.ApiRequestUtility

// Docs: https://documenter.getpostman.com/view/664302/S1ENwy59#674e81c1-6a0c-4836-8a7e-6ea1fe8e6677

//The below code was derived from eBird API
//https://documenter.getpostman.com/view/664302/S1ENwy59

object EBirdApi {

    private const val baseUrl = "https://api.ebird.org"
    private const val version = "/v2"
    private const val hotspots = "/ref/hotspot"

    private val builder = Uri.parse("$baseUrl$version$hotspots")

    // Key https://ebird.org/api/request
    private const val key = "k1j2h3g4f5d6s7a8"


    suspend fun fetchNearbyHotspots(
        lat: Double, lng: Double, distanceInKm: Float,
    ): Result<Array<EBirdLocation>> {

        // TODO: Validation
        //Name	Values	    Default	Description
        //back	1-30	    (none)	Only fetch hotspots which have been visited up to 'back' days ago.
        //dist	0 - 500	    25      The search radius from the given position, in kilometers.
        //fmt	csv, json	csv	    Fetch the records in CSV or JSON format.
        //lat	-90 - 90		    Required. Latitude to 2 decimal places.
        //lng	-180 - 180		    Required. Longitude to 2 decimal places.

        // This is the same as the above
        /*val uri = builder.buildUpon().appendPath("geo").appendQueryParameter("lat", lat.toString())
            .appendQueryParameter("lng", lng.toString())
            .appendQueryParameter("dist", distance.toString()).appendQueryParameter("fmt", "json")
            .build()*/

        val url = "$baseUrl$version$hotspots/geo?lat=$lat&lng=$lng&dist=$distanceInKm&fmt=json"
        return ApiRequestUtility.makeRequestJson(url)
    }

    suspend fun fetchHotspotInfo(
        hotspotId: String,
    ): Result<EBirdHotspotData> {

     /*   val uri =
            builder.buildUpon().appendPath("info").appendPath(hotspotId)
                .appendQueryParameter("fmt", "json")
                .build()*/

        val headers = mapOf("X-eBirdApiToken" to key)
        val url = "$baseUrl$version$hotspots/info/$hotspotId?&fmt=json"
        return ApiRequestUtility.makeRequestJson(url, headers = headers)
    }

    suspend fun fetchHotspotsInRegion(
        code: String,
    ): Result<Array<EBirdLocation>> {
        // Code: The country, subnational1 or subnational2 code.

//        val uri = builder.buildUpon().appendPath(code).appendQueryParameter("fmt", "json").build()
        val url = "$baseUrl$version$hotspots/$code?&fmt=json"
        return ApiRequestUtility.makeRequestJson(url)
    }

}