package com.ryanblignaut.featherfinder.ui.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.model.BirdObservation
import com.ryanblignaut.featherfinder.model.FullBirdObservation
import java.text.SimpleDateFormat
import java.util.Locale

class ObsInfoAdapter (private val context: Context) : GoogleMap.InfoWindowAdapter {

    override fun getInfoContents(marker: Marker): View? {
        val obsItem = marker.tag as? FullBirdObservation ?: return null

        val view = LayoutInflater.from(context).inflate(
            R.layout.obs_marker_details, null)
        view.findViewById<TextView>(
            R.id.obsTitle
        ).text = obsItem.birdSpecies
        view.findViewById<TextView>(
            R.id.obsDate
        ).text = obsItem.date
        view.findViewById<TextView>(
            R.id.obsTime
        ).text = obsItem.time
        view.findViewById<TextView>(
            R.id.obsNotes
        ).text = obsItem.notes
        return view
    }
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }
}