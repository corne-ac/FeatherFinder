package com.ryanblignaut.featherfinder.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentMapBinding
import com.ryanblignaut.featherfinder.model.api.EBirdLocation
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.viewmodel.BirdingHotspotViewModel


private const val REQUEST_LOCATION_PERMISSIONS_CODE = 123
private val PERMISSIONS_REQUIRED =
    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

class NearbyBirding : PreBindingFragment<FragmentMapBinding>(), OnMapReadyCallback {
    private lateinit var birdingHotspotViewModel: BirdingHotspotViewModel
    override fun addContentToView(savedInstanceState: Bundle?) {

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var isValid = true
            permissions.entries.forEach {
                println("${it.key} = ${it.value}")
                if (!it.value) isValid = false
            }

            if (isValid) {
                showMap(null)
            } else
                MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                    .setMessage("Location permissions are required to use this feature.")
                    .setCancelable(true).show()

        }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
//        val googleMap =
//            childFragmentManager.findFragmentById(com.ryanblignaut.featherfinder.R.id.map) as SupportMapFragment?

        //check for permissions
        when {
            hasLocationPermissions() -> {
                showMap(savedInstanceState)
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected, and what
                // features are disabled if it's declined. In this UI, include a
                // "cancel" or "no thanks" button that lets the user continue
                // using your app without granting the permission.
//                TODO("Show a dialog explaining why the user should grant the location permission")
                MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                    .setMessage("Location permissions are required to use this feature.")
                    .setCancelable(true).show()
                // When the user clicks "ok", request the permission again.
                requestPermissionLauncher.launch(PERMISSIONS_REQUIRED)

            }

            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(PERMISSIONS_REQUIRED)
            }
        }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS_REQUIRED, 1)
    }

    private fun hasLocationPermissions(): Boolean {
        for (permission in PERMISSIONS_REQUIRED) {
            if (checkPermission(permission)) return false
        }
        return true
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(), permission
        ) != PackageManager.PERMISSION_GRANTED
    }


    private fun showMap(savedInstanceState: Bundle?) {
        val googleMap = binding.map.getFragment<SupportMapFragment>()
        googleMap.onCreate(savedInstanceState)
        googleMap.getMapAsync(this)
    }

    override fun inflateBindingSelf(
        inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean,
    ): FragmentMapBinding {
        return inflateBinding(inflater, container)
    }

    override fun onMapReady(map: GoogleMap) {
        // Check if the user has given permission to use their location.
//        registerForActivityResult.launch(PERMISSIONS_REQUIRED)

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
//                    (activity as LoginActivity).loadFragment(RouteBirding(position))

                    Bundle().apply {
                        putDouble("lat", position.latitude)
                        putDouble("lng", position.longitude)
                    }.also { bundle ->
                        findNavController().navigate(R.id.navigation_route, bundle)
                    }


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