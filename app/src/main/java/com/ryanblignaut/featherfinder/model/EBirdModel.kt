package com.ryanblignaut.featherfinder.model

import com.google.gson.annotations.SerializedName

// Generated using https://json2kt.com/
// @formatter:off

data class EBirdLocation(

    @SerializedName("locId"             ) var locId             : String? = null,
    @SerializedName("locName"           ) var locName           : String? = null,
    @SerializedName("countryCode"       ) var countryCode       : String? = null,
    @SerializedName("subnational1Code"  ) var subnational1Code  : String? = null,
    @SerializedName("lat"               ) var lat               : Double? = null,
    @SerializedName("lng"               ) var lng               : Double? = null,
    @SerializedName("latestObsDt"       ) var latestObsDt       : String? = null,
    @SerializedName("numSpeciesAllTime" ) var numSpeciesAllTime : Int?    = null

)

data class EBirdHotspotData (

    @SerializedName("locId"            ) var locId            : String?  = null,
    @SerializedName("name"             ) var name             : String?  = null,
    @SerializedName("latitude"         ) var latitude         : Double?  = null,
    @SerializedName("longitude"        ) var longitude        : Double?  = null,
    @SerializedName("countryCode"      ) var countryCode      : String?  = null,
    @SerializedName("countryName"      ) var countryName      : String?  = null,
    @SerializedName("subnational1Name" ) var subnational1Name : String?  = null,
    @SerializedName("subnational1Code" ) var subnational1Code : String?  = null,
    @SerializedName("subnational2Code" ) var subnational2Code : String?  = null,
    @SerializedName("subnational2Name" ) var subnational2Name : String?  = null,
    @SerializedName("isHotspot"        ) var isHotspot        : Boolean? = null,
    @SerializedName("locName"          ) var locName          : String?  = null,
    @SerializedName("lat"              ) var lat              : Double?  = null,
    @SerializedName("lng"              ) var lng              : Double?  = null,
    @SerializedName("hierarchicalName" ) var hierarchicalName : String?  = null,
    @SerializedName("locID"            ) var locID            : String?  = null

)
// @formatter:on
