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
                R.id.navigation_add_observation,
                R.id.navigation_all_goals
            )
        )

        //Hide bottom nav on certain screens

        //The below hide code snippet was derived from StackOverflow
        //https://stackoverflow.com/questions/56461156/how-to-hide-the-bottom-navigation-bar-in-certain-fragments
        //Skyrela
        //https://stackoverflow.com/users/17714915/skyrela

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val id = destination.id
            val hideBottomList = listOf(
                R.id.navigation_add_goal, R.id.navigation_add_observation,
                R.id.navigation_settings,
                R.id.navigation_achievement, R.id.navigation_observation_detail
            )
            binding.navView.visibility = (if (hideBottomList.contains(id)) View.GONE else View.VISIBLE)
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}