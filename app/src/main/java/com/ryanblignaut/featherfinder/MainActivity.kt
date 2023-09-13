package com.ryanblignaut.featherfinder

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ryanblignaut.featherfinder.databinding.ActivityMainMenuBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.hide()
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_bird_info,
                R.id.navigation_home,
                R.id.navigation_nearby_hotspots,
                R.id.navigation_observation_list,
                R.id.navigation_profile,
                R.id.navigation_add_observation
            )
        )
        val fadeAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade)

        //Hide bottom nav on certain screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val id = destination.id
            fadeAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    // Animation started, you can add any necessary actions here
                }

                override fun onAnimationEnd(animation: Animation) {
                    // Animation ended, change the visibility based on your conditions
                    if (id == R.id.navigation_add_goal || id == R.id.navigation_add_observation) {
                        binding.navView.visibility = View.GONE
                    } else {
                        binding.navView.visibility = View.VISIBLE
                    }
                }

                override fun onAnimationRepeat(animation: Animation) {
                    // Animation repeated, if needed
                }
            })
            binding.navView.startAnimation(fadeAnimation)

            /*  if (id == R.id.navigation_add_goal || id == R.id.navigation_add_observation) {
                  binding.navView.startAnimation(fadeAnimation).also {
                      binding.navView.visibility = View.GONE
                  }

              } else {
                  binding.navView.startAnimation(fadeAnimation).also {
                      binding.navView.visibility = View.VISIBLE
                  }
              }*/
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}