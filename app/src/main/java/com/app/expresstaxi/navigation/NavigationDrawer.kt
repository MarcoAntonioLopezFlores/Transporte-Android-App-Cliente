package com.app.expresstaxi.navigation

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.app.expresstaxi.LoginActivity
import com.app.expresstaxi.R
import com.app.expresstaxi.databinding.ActivityNavigationDrawerBinding
import kotlinx.android.synthetic.main.activity_navigation_drawer.view.*

class NavigationDrawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_container)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mapsFragment, R.id.myServicesTakenFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.nav_signOut).setOnMenuItemClickListener {
            signOut()
            true
        }
        //navView.setNavigationItemSelectedListener(this)
    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_main_drawer, menu)
        return true
    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun signOut(){
        startActivity(Intent(applicationContext,LoginActivity::class.java))
        finish()
    }
}