package com.ryanblignaut.featherfinder.ui.map

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ryanblignaut.featherfinder.LoginActivity
import com.ryanblignaut.featherfinder.databinding.FragmentMapBinding
import com.ryanblignaut.featherfinder.model.api.EBirdLocation
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.BirdingHotspotViewModel


class NearbyBirding : PreBindingFragment<FragmentMapBinding>(), OnMapReadyCallback {
    private lateinit var birdingHotspotViewModel: BirdingHotspotViewModel

    override fun addContentToView(savedInstanceState: Bundle?) {
        val googleMap =
            childFragmentManager.findFragmentById(com.ryanblignaut.featherfinder.R.id.map) as SupportMapFragment?
//        val googleMap = binding.mapView
        googleMap?.onCreate(savedInstanceState)
        googleMap?.getMapAsync(this);
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentMapBinding {
        return inflateBinding(inflater, container)
    }


    override fun onMapReady(map: GoogleMap) {
        birdingHotspotViewModel = ViewModelProvider(this)[BirdingHotspotViewModel::class.java]
        val convertBirdLocationOntoMap = function(map)
        birdingHotspotViewModel.live.observe(viewLifecycleOwner, convertBirdLocationOntoMap)
        birdingHotspotViewModel.fetchHotspots(-25.873390, 28.197800)

    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun function(map: GoogleMap): (Result<Array<EBirdLocation>>) -> Unit {
        return {
            var p = LatLng(40.730610, -73.935242);
            map.clear()


//                .fillColor(Color.parseColor("#220000FF"))


            if (it.isSuccess) {
                it.getOrNull()!!.forEach { hotspot ->
                    // Skip the location if its latitude or longitude is null.
                    if (hotspot.lat == null || hotspot.lng == null) return@forEach
                    p = LatLng(hotspot.lat!!, hotspot.lng!!)
                    val markerOptions =
                        MarkerOptions().position(LatLng(hotspot.lat!!, hotspot.lng!!))
                            .title(hotspot.locName)

                    map.addMarker(markerOptions)
                }

                map.moveCamera(CameraUpdateFactory.newLatLng(p))
                //TODO: Add a circle around the users current location
                val circleOptions = CircleOptions().center(p).radius(10000.0) // Radius in meters
                    .strokeColor(Color.BLUE)
                map.addCircle(circleOptions)

                // TODO: Move to where we are on the map.

                map.setOnMarkerClickListener { marker ->
                    val position = marker.position
                    // Move to the next fragment and pass in the position
                    (activity as LoginActivity).loadFragment(RouteBirding(position))

                    //                val markerTitle = marker.title
                    //                val markerSnippet = marker.snippet
                    //                println(markerTitle)
                    true
                }



                map.moveCamera(CameraUpdateFactory.zoomTo(11F));
                map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            } else MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                .setMessage(it.exceptionOrNull()?.message ?: "Unknown error").setCancelable(true)
                .show()

        }
    }
}