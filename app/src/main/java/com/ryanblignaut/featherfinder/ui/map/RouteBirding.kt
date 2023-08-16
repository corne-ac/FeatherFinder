package com.ryanblignaut.featherfinder.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.ryanblignaut.featherfinder.databinding.FragmentMapBinding
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.RouteViewModel


class RouteBirding(private val positionIn: LatLng) : PreBindingFragment<FragmentMapBinding>(),
    OnMapReadyCallback {
    private lateinit var routeViewModel: RouteViewModel

    override fun addContentToView(savedInstanceState: Bundle?) {
        routeViewModel = ViewModelProvider(this)[RouteViewModel::class.java]

        val googleMap =
            childFragmentManager.findFragmentById(com.ryanblignaut.featherfinder.R.id.map) as SupportMapFragment?
        googleMap?.onCreate(savedInstanceState)
        googleMap?.getMapAsync(this);
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean
    ): FragmentMapBinding {
        return inflateBinding(inflater, container)
    }

    override fun onMapReady(map: GoogleMap) {

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
            return onMapReady(map)
        }
        map.isMyLocationEnabled = true

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success: Boolean = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), com.ryanblignaut.featherfinder.R.raw.style_json
                )
            )
            if (!success) {
                Log.e("e", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("a", "Can't find style. Error: ", e)
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            val currentLocation = LatLng(location.latitude, location.longitude)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 11f))

            val startLat = currentLocation.latitude
            val startLong = currentLocation.longitude
            val endLat = positionIn.latitude
            val endLong = positionIn.longitude

            Log.d("FF_APP", "onMapReady: $startLat, $startLong, $endLat, $endLong")
            routeViewModel.fetchRoute(startLong, startLat, endLong, endLat)
            map.addMarker(MarkerOptions().position(positionIn))
        }

        routeViewModel.live.observe(viewLifecycleOwner) {
            if (it.isSuccess) {
                val layer = GeoJsonLayer(map, it.getOrNull())
                layer.defaultLineStringStyle.color = 0xFF0000FF.toInt()
                layer.addLayerToMap()
                layer.defaultLineStringStyle.width = 15f
            } else {
                MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                    .setMessage(it.exceptionOrNull()?.message ?: "Unknown error")
                    .setCancelable(true).show()
            }
        }
    }
}