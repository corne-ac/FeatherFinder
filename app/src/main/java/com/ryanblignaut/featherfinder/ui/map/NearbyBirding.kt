package com.ryanblignaut.featherfinder.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ryanblignaut.featherfinder.R
import com.ryanblignaut.featherfinder.databinding.FragmentMapBinding
import com.ryanblignaut.featherfinder.model.api.EBirdLocation
import com.ryanblignaut.featherfinder.ui.component.ext.setOnClickListener
import com.ryanblignaut.featherfinder.ui.helper.PreBindingFragment
import com.ryanblignaut.featherfinder.utils.SettingReferences
import com.ryanblignaut.featherfinder.viewmodel.BirdingHotspotViewModel
import com.ryanblignaut.featherfinder.viewmodel.observation.AllObservationsViewModel
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale


private val PERMISSIONS_REQUIRED =
    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

class NearbyBirding : PreBindingFragment<FragmentMapBinding>(), OnMapReadyCallback {
    private lateinit var allObservationsViewModel: AllObservationsViewModel
    private lateinit var birdingHotspotViewModel: BirdingHotspotViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient //Used to get user location
    private lateinit var locationCallback: LocationCallback //Used for location services
    override fun addContentToView(savedInstanceState: Bundle?) {

    }

    //The below code was derived from StackOverflow
    //https://stackoverflow.com/questions/73864190/location-permission-does-not-always-show-up
    //Primo
    //https://stackoverflow.com/users/8101634/primo

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        var isValid = true
        permissions.entries.forEach {
            println("${it.key} = ${it.value}")
            if (!it.value) isValid = false
        }

        if (isValid) {
            showMap(null)
        } else MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
            .setMessage("Location permissions are required to use this feature.")
            .setCancelable(true).show()

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext()) //initialise for retrieving location later

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
        allObservationsViewModel = ViewModelProvider(this)[AllObservationsViewModel::class.java]
        val convertBirdLocationOntoMap = addItems(map)
        birdingHotspotViewModel.live.observe(viewLifecycleOwner, convertBirdLocationOntoMap)
        var userLocation: LatLng = LatLng(0.0, 0.0)
        //Get user location and fetch hotspots according to it
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> //Can return null
            if (location != null) {
                userLocation = LatLng(location.latitude, location.longitude)
                val userLoc = LatLng(location.latitude, location.longitude)
                birdingHotspotViewModel.fetchHotspots(
                    userLoc.latitude, userLoc.longitude, SettingReferences.getMaxDistance()
                )
            map.isMyLocationEnabled = true} else {
                //Try to start location service, as it is most common cause for a null return
                locServiceTry(map)
            }

        }.addOnFailureListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle("Location Retrieval Error")
                .setMessage(it.message ?: "Unknown error").setCancelable(true).show()
        }

        binding.btnLoadObs.setOnClickListener {
            addObsItems(map)
        }

        binding.btnLoadNearby.setOnClickListener {
            addItems(map)
            birdingHotspotViewModel.fetchHotspots(userLocation.latitude, userLocation.longitude, SettingReferences.getMaxDistance())
        }
    }

    @SuppressLint("MissingPermission")
    private fun locServiceTry(map: GoogleMap) {
        // 1. Start the location service
        val locationRequest = LocationRequest.Builder(10000) // 10 seconds
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // 100
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // 3. Use the location for further processing
                    val userLoc = LatLng(location.latitude, location.longitude)
                    birdingHotspotViewModel.fetchHotspots(
                        userLoc.latitude, userLoc.longitude, SettingReferences.getMaxDistance()
                    )
                    map.isMyLocationEnabled = true
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
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    @SuppressLint("MissingPermission", "SimpleDateFormat", "PotentialBehaviorOverride")
    private fun addObsItems(map: GoogleMap) {
        map.clear()
        //Get observations
        allObservationsViewModel.getObservationsLocations()
        allObservationsViewModel.liveDetails.observe(viewLifecycleOwner) {
            if (it.isFailure) {
                return@observe
            }
            val list = it.getOrNull()!!

            //Add markers
            val boundsBuilder = LatLngBounds.Builder()

            list.forEach { entry ->
                val p = LatLng( entry.lat.toDouble(), entry.long.toDouble())
                boundsBuilder.include(p)

                // Marker will be green if it has had an observation in the last 30 days, orange if it is older than 30 days and less than 6 months, violet if older than 6 months and azure if the date cant be parsed/is null.
                val markerOptions = MarkerOptions().position(p).title(entry.birdSpecies)

                markerOptions.icon(
                    BitmapDescriptorFactory.defaultMarker(getColorBasedOnDateWithSimpleDateFormat(
                        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH), entry.date))
                )

                map.addMarker(markerOptions)?.tag = entry
            }

            // Here we are taking the bounds of the markers and zooming in based on that.
            map.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    boundsBuilder.build(), 100
                )
            )


            map.isMyLocationEnabled = true

            // Show dialog
            map.setInfoWindowAdapter(ObsInfoAdapter(requireContext()))
            //Set empty onl;
            map.setOnMarkerClickListener { false }
        }





        map.mapType = GoogleMap.MAP_TYPE_TERRAIN
    }

    @SuppressLint("PotentialBehaviorOverride", "MissingPermission")
    private fun addItems(map: GoogleMap): (Result<Array<EBirdLocation>>) -> Unit {
        return { result ->
            var userLocation: LatLng?
            val radius = SettingReferences.getMaxDistance() * 1000 /*10000.0*/

            //Get user's last known location
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> // Can return null
                location?.let {
                    userLocation = LatLng(location.latitude, location.longitude)

                    //Handle the result of fetching eBird locations
                    result.onSuccess { hotspots ->

                        // Sometimes the eBird API returns a value that would be outside the radius so we will filter them out for now.
                        val markersInsideRadius =
                            hotspots.filter { it.lat != null && it.lng != null }.filter { hotspot ->
                                val hotspotLocation = LatLng(hotspot.lat!!, hotspot.lng!!)
                                userLocation != null && calculateDistance(
                                    userLocation!!, hotspotLocation
                                ) <= radius
                            }

                        map.clear()
                        map.isMyLocationEnabled = true

                        val boundsBuilder = LatLngBounds.Builder()

                        //Add markers for hotspots inside the radius
                        markersInsideRadius.forEach { hotspot ->
                            val p = LatLng(hotspot.lat!!, hotspot.lng!!)
                            boundsBuilder.include(p)

                            // Implemented feedback on -> View birding hotspots on map
                            // Marker will be green if it has had an observation in the last 30 days, orange if it is older than 30 days and less than 6 months, violet if older than 6 months and azure if the date cant be parsed/is null.
                            val markerOptions = MarkerOptions().position(p).title(hotspot.locName)

                            if (hotspot.latestObsDt != null) markerOptions.icon(
                                BitmapDescriptorFactory.defaultMarker(getColorBasedOnDate(hotspot.latestObsDt!!))
                            )

                            map.addMarker(markerOptions)
                        }
                        // If there are no markers inside the radius, then we will just add the user's location.
                        // Not too sure if this is necessary but I assume that the map will not have any bounds and wont zoom if no markers are around.
                        if (markersInsideRadius.isEmpty()) {
                            boundsBuilder.include(userLocation!!);
                        }

                        // Implemented feedback on -> Display user’s current position on map.
                        // Here we are taking the bounds of the markers and zooming in based on that. The whole circle might not be fully visible yet all the markers will be in focus with 100 units of padding around them.
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                boundsBuilder.build(), 100
                            )
                        )

                        //Add a circle around user's location
                        userLocation?.let { loc ->
                            val circleOptions = CircleOptions().center(loc)
                                .radius(radius.toDouble()) // Radius in meters
                                .strokeColor(Color.BLUE)
                            map.addCircle(circleOptions)
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

                        map.mapType = GoogleMap.MAP_TYPE_TERRAIN
                    }

                    result.onFailure { exception ->
                        MaterialAlertDialogBuilder(requireContext()).setTitle("Error")
                            .setMessage(exception.message ?: "Unknown error").setCancelable(true)
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
            point1.latitude, point1.longitude, point2.latitude, point2.longitude, results
        )
        return results[0]
    }

    // We know the date format of the ebird observation is "yyyy-MM-dd HH:mm" so simple date format can be used to parse the date without needing about locate.
    @SuppressLint("SimpleDateFormat")
    private fun getColorBasedOnDate(inputDateStr: String): Float {
        // Define the date format for parsing the ebird input string.
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

        return getColorBasedOnDateWithSimpleDateFormat(dateFormat, inputDateStr)
    }

    private fun getColorBasedOnDateWithSimpleDateFormat(simpleDateFormat: SimpleDateFormat, inputDateStr: String): Float {
        try {
            // Parse the input string into a Date object.
            val inputDate = simpleDateFormat.parse(inputDateStr)

            // Calculate the time difference in milliseconds.
            val currentTime = System.currentTimeMillis()
            val dateDifference = currentTime - inputDate.time

            // Calculate the number of days and months
            val daysDifference = dateDifference / (1000 * 60 * 60 * 24)
            val monthsDifference = daysDifference / 30

            // Assign colors based on the time difference
            return if (daysDifference >= 30) {
                when {
                    monthsDifference >= 6 -> BitmapDescriptorFactory.HUE_VIOLET // More than 6 months ago
                    else -> BitmapDescriptorFactory.HUE_ORANGE // Between 30 days and 6 months ago
                }
            } else {
                BitmapDescriptorFactory.HUE_GREEN // Less than 30 days ago
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return BitmapDescriptorFactory.HUE_AZURE // Default color in case of parsing error
        }
    }

}