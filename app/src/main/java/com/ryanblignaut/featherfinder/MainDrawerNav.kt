package com.ryanblignaut.featherfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.ryanblignaut.featherfinder.databinding.ActivityMainDrawerNavBinding


class MainDrawerNav : AppCompatActivity() {

    private lateinit var binding: ActivityMainDrawerNavBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*

        binding = ActivityMainDrawerNavBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMainDrawerNav.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
        findNavController(R.id.nav_host_fragment_content_main_drawer_nav)
        setupActionBarWithNavController()
*/

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        /*        appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.nav_home,
                        R.id.nav_categories,
                        R.id.nav_category_create,
                        R.id.nav_timesheet_create,
                        R.id.nav_all_timesheet,
                        R.id.nav_goals,
                        R.id.nav_total_hours_worked_graph,
                        R.id.nav_monthly_goal_progress_tracker
                    ), drawerLayout
                )

                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)*/

        /*  // Animation options.
          val navOptions: NavOptions =
              NavOptions.Builder().setLaunchSingleTop(true).setEnterAnim(android.R.anim.slide_in_left)
                  .setExitAnim(android.R.anim.slide_out_right)
                  .setPopEnterAnim(android.R.anim.slide_in_left)
                  .setPopExitAnim(android.R.anim.slide_out_right)
                  .setPopUpTo(navController.graph.startDestinationId, false).build()

          // Set up fancy nav options that will play animations when navigating between fragments.
          navView.setNavigationItemSelectedListener { item ->
              var handled = false
              if (navController.graph.findNode(item.itemId) != null) {
                  navController.navigate(item.itemId, null, navOptions)
                  handled = true
              } else {
              }
              if (handled) {
                  val menu: Menu = binding.navView.menu
                  var h = 0
                  val size = menu.size()
                  while (h < size) {
                      val menuItem = menu.getItem(h)
                      menuItem.isChecked = menuItem.itemId == item.itemId
                      h++
                  }
              }
              binding.drawerLayout.closeDrawer(GravityCompat.START)
              false
          }*/

        /*
                // Set up the user icon and username in the drawer.
                val headerView = navView.getHeaderView(0)
                val userName = headerView.findViewById<TextView>(R.id.username)
                val icon = headerView.findViewById<ImageView>(R.id.userIcon)
        //TODO: figure out fast refresh
                if (auth.currentUser != null) {

                    userName.text = auth.currentUser!!.displayName
                    //set icon
                    if (auth.currentUser!!.photoUrl != null){
                        icon.setImageURI(auth.currentUser!!.photoUrl)
                    }
                }

        //        icon.setImageResource(R.drawable.ic_baseline_account_circle_24)

                icon.setOnClickListener { view ->
                    val popupMenu = PopupMenu(this, view)
                    popupMenu.menuInflater.inflate(R.menu.main_drawer_nav, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {
                        addOptionsToMenu(it)
                    }
                    popupMenu.show()
                }
        */

    }


    /*    override fun onCreateOptionsMenu(menu: Menu): Boolean {
            // Inflate the menu; this adds items to the action bar if it is present.
            menuInflater.inflate(R.menu.main_drawer_nav, menu)
            menu.forEach {
                it.setOnMenuItemClickListener { menuItem ->
                    addOptionsToMenu(menuItem)
                }
            }
            return true
        }*/

    /*   override fun onSupportNavigateUp(): Boolean {
           val navController = findNavController(R.id.nav_host_fragment_content_main_drawer_nav)
           return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
       }*/
}