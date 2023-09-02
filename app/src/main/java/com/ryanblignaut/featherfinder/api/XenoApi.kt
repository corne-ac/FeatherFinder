package com.ryanblignaut.featherfinder.api

import com.ryanblignaut.featherfinder.model.api.XenoResponse
import com.ryanblignaut.featherfinder.utils.ApiRequestUtility

object XenoApi {
    private const val baseUrl = "https://xeno-canto.org/api"
    private const val version = "/2"
    private const val query = "/recordings"
    // Geographic coordinates
    // lat:-12.234 lon:-69.98
    // box:LAT_MIN,LON_MIN,LAT_MAX,LON_MAX

    // Location
    // loc:pretoria
    suspend fun fetchBirdsInLocation(loc: String): Result<XenoResponse> {
        val url = "$baseUrl$version$query?query=loc:$loc"
        return ApiRequestUtility.makeRequestJson(url)
    }
}