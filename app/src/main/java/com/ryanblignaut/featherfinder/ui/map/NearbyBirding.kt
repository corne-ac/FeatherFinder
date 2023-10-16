package com.ryanblignaut.featherfinder.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
    private lateinit var fusedLocationClient: FusedLocationProviderClient //Used to get user location
    private lateinit var locationCallback: LocationCallback //Used for location services
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext()) //initialise for retrieving location later

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
//                MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
//                    .setMessage("Location permissions are required to use this feature.")
//                    .setCancelable(true).show()
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

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {

        birdingHotspotViewModel = ViewModelProvider(this)[BirdingHotspotViewModel::class.java]
        val convertBirdLocationOntoMap = function(map)
        birdingHotspotViewModel.live.observe(viewLifecycleOwner, convertBirdLocationOntoMap)

        //Get user location and fetch hotspots according to it
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? -> //Can return null
                if (location != null) {
                    val userLoc = LatLng(location.latitude, location.longitude)
                    birdingHotspotViewModel.fetchHotspots(userLoc.latitude, userLoc.longitude)
                } else {
                    //Try to start location service, as it is most common cause for a null return
                    locServiceTry()
                }

            }
            .addOnFailureListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Location Retrieval Error")
                    .setMessage(it.message ?: "Unknown error")
                    .setCancelable(true)
                    .show()
            }

    }

    @SuppressLint("MissingPermission")
    private fun locServiceTry() {
        // 1. Start the location service
        val locationRequest = LocationRequest.Builder(10000) // 10 seconds
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // 100
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // 3. Use the location for further processing
                    val userLoc = LatLng(location.latitude, location.longitude)
                    birdingHotspotViewModel.fetchHotspots(userLoc.latitude, userLoc.longitude)

                    // Stop location updates after the first successful retrieval
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                }
                //Dialog should only show if fail
//                // Show a dialog indicating that no location was found
//                MaterialAlertDialogBuilder(requireContext())
//                    .setTitle("Location Service Error")
//                    .setMessage("Please enable a location service on device")
//                    .setCancelable(true)
//                    .show()
            }
        }

        // 2. Retrieve the location
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @SuppressLint("PotentialBehaviorOverride", "MissingPermission")
    private fun function(map: GoogleMap): (Result<Array<EBirdLocation>>) -> Unit {
        return { result ->
            var userLocation: LatLng? = null
            val radius = 10000.0

            //Get user's last known location
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? -> // Can return null
                    location?.let {
                        userLocation = LatLng(location.latitude, location.longitude)

                        //Handle the result of fetching eBird locations
                        result.onSuccess { hotspots ->
                            val markersInsideRadius = hotspots
                                .filter { it.lat != null && it.lng != null }
                                .filter { hotspot ->
                                    val hotspotLocation = LatLng(hotspot.lat!!, hotspot.lng!!)
                                    userLocation != null &&
                                            calculateDistance(userLocation!!, hotspotLocation) <= radius
                                }

                            map.clear()
                            map.isMyLocationEnabled = true

                            //Add markers for hotspots inside the radius
                            markersInsideRadius.forEach { hotspot ->
                                val p = LatLng(hotspot.lat!!, hotspot.lng!!)
                                val markerOptions = MarkerOptions().position(p).title(hotspot.locName)
                                map.addMarker(markerOptions)
                            }

                            //Add a circle around user's location
                            userLocation?.let { loc ->
                                val circleOptions = CircleOptions()
                                    .center(loc)
                                    .radius(radius) // Radius in meters
                                    .strokeColor(Color.BLUE)
                                map.addCircle(circleOptions)

                                map.moveCamera(CameraUpdateFactory.newLatLng(loc))
                            }

                            //Set click listener for markers
                            map.setOnMarkerClickListener { marker ->
                                val position = marker.position
                                val bundle = Bundle().apply {
                                    putDouble("lat", position.latitude)
                                    putDouble("lng", position.longitude)
                                }
                                findNavController().navigate(R.id.navigation_route, bundle)
                                true
                            }

                            map.moveCamera(CameraUpdateFactory.zoomTo(11F))
                            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                        }

                        result.onFailure { exception ->
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Error")
                                .setMessage(exception.message ?: "Unknown error")
                                .setCancelable(true)
                                .show()
                        }
                    }
                }
        }
    }


    // Function to calculate distance between two LatLng points in meters
    private fun calculateDistance(point1: LatLng, point2: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            point1.latitude,
            point1.longitude,
            point2.latitude,
            point2.longitude,
            results
        )
        return results[0]
    }


}