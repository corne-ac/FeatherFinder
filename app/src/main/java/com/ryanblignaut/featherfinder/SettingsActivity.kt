package com.ryanblignaut.featherfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.ryanblignaut.featherfinder.ui.auth.Login
import com.ryanblignaut.featherfinder.ui.map.NearbyBirding


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
   /*     val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val mapFragment = NearbyBirding()
        fragmentTransaction.replace(R.id.fragmentContainer, mapFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()*/
        val lo = Login()
        loadFragment(lo)

    }

    fun loadFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        // Replace the content of the container with the new fragment
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    /*   private lateinit var birdingHotspotViewModel: BirdingHotspotViewModel
       override fun onCreate(savedInstanceState: Bundle?) {
           super.onCreate(savedInstanceState)
           setContentView(R.layout.activity_maps)
           val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
           mapFragment?.getMapAsync(this)
       }

       override fun onMapReady(googleMap: GoogleMap) {

           birdingHotspotViewModel = ViewModelProvider(this)[BirdingHotspotViewModel::class.java]


   //        birdingHotspotViewModel.hotspots.observe(viewLifecycleOwner, Observer { hotspots ->
   //            // Clear existing markers
   //            googleMap.clear()
   //
   //            // Add markers for each hotspot
   //            hotspots.forEach { hotspot ->
   //                val markerOptions = MarkerOptions()
   //                    .position(LatLng(hotspot.lat!!, hotspot.lng!!))
   //                    .title(hotspot.locName)
   //                googleMap.addMarker(markerOptions)
   //            }
   //        })

           *//*   val handler = Handler(Looper.getMainLooper())
        val thread = Thread {
            try {
                val api = OpenRoutesApi()
                val route = api.fetchRoute(40.730610, -73.935242, -1)
                if (route.isSuccess) {
                    handler.post {
                        // Run this part on the ui thread
                        val layer = GeoJsonLayer(googleMap, route.get?.let { JSONObject(it) })
                        println(layer)
                        layer.addLayerToMap()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*//*

//        thread.start()


//        val layer = GeoJsonLayer()
//        layer.addLayerToMap()
        *//* val wrong1 = LatLng(28.16059, -25.890457)
         val wrong2 = LatLng(28.14248, -25.874163)
         googleMap.addMarker(MarkerOptions().position(wrong1).title("wrong1"))
         googleMap.addMarker(MarkerOptions().position(wrong2).title("wrong2"))

         val sydney = LatLng(-25.890457, 28.16059)
         googleMap.addMarker(
             MarkerOptions().position(sydney).title("Marker in Sydney")
         )
         val sydney1 = LatLng(-25.874163, 28.14248)

         googleMap.addMarker(
             MarkerOptions().position(sydney1).title("Test position")
         )

         googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*//*
    }*/

}