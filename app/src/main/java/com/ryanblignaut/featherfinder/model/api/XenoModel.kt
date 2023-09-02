package com.ryanblignaut.featherfinder.model.api

import com.google.gson.annotations.SerializedName

// Generated using https://json2kt.com/
// @formatter:off
data class XenoResponse (

    @SerializedName("numRecordings" ) var numRecordings : String?               = null,
    @SerializedName("numSpecies"    ) var numSpecies    : String?               = null,
    @SerializedName("page"          ) var page          : Int?                  = null,
    @SerializedName("numPages"      ) var numPages      : Int?                  = null,
    @SerializedName("recordings"    ) var recordings    : ArrayList<XenoRecording> = arrayListOf()

)
data class XenoRecording (

    @SerializedName("id"            ) var id            : String?           = null,
    @SerializedName("gen"           ) var gen           : String?           = null,
    @SerializedName("sp"            ) var sp            : String?           = null,
    @SerializedName("ssp"           ) var ssp           : String?           = null,
    @SerializedName("group"         ) var group         : String?           = null,
    @SerializedName("en"            ) var en            : String?           = null,
    @SerializedName("rec"           ) var rec           : String?           = null,
    @SerializedName("cnt"           ) var cnt           : String?           = null,
    @SerializedName("loc"           ) var loc           : String?           = null,
    @SerializedName("lat"           ) var lat           : String?           = null,
    @SerializedName("lng"           ) var lng           : String?           = null,
    @SerializedName("alt"           ) var alt           : String?           = null,
    @SerializedName("type"          ) var type          : String?           = null,
    @SerializedName("sex"           ) var sex           : String?           = null,
    @SerializedName("stage"         ) var stage         : String?           = null,
    @SerializedName("method"        ) var method        : String?           = null,
    @SerializedName("url"           ) var url           : String?           = null,
    @SerializedName("file"          ) var file          : String?           = null,
    @SerializedName("file-name"     ) var fileName     : String?           = null,
    @SerializedName("lic"           ) var lic           : String?           = null,
    @SerializedName("q"             ) var q             : String?           = null,
    @SerializedName("length"        ) var length        : String?           = null,
    @SerializedName("time"          ) var time          : String?           = null,
    @SerializedName("date"          ) var date          : String?           = null,
    @SerializedName("uploaded"      ) var uploaded      : String?           = null,
    @SerializedName("also"          ) var also          : ArrayList<String> = arrayListOf(),
    @SerializedName("rmk"           ) var rmk           : String?           = null,
    @SerializedName("bird-seen"     ) var birdSeen     : String?           = null,
    @SerializedName("animal-seen"   ) var animalSeen   : String?           = null,
    @SerializedName("playback-used" ) var playbackUsed : String?           = null,
    @SerializedName("temp"          ) var temp          : String?           = null,
    @SerializedName("regnr"         ) var regnr         : String?           = null,
    @SerializedName("auto"          ) var auto          : String?           = null,
    @SerializedName("dvc"           ) var dvc           : String?           = null,
    @SerializedName("mic"           ) var mic           : String?           = null,
    @SerializedName("smp"           ) var smp           : String?           = null
)
// @formatter:on
