package com.app.expresstaxi.navigation

import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.expresstaxi.LoginActivity
import com.app.expresstaxi.R
import com.app.expresstaxi.databinding.ActivityNavigationDrawerBinding
import com.app.expresstaxi.fragments.DetailsDriverFragment
import com.app.expresstaxi.preferences.PrefsApplication
import com.app.expresstaxi.utils.locationback.LocationUpdatesService
import com.app.expresstaxi.utils.locationback.StatusLocation
import kotlinx.android.synthetic.main.nav_header_navigation_drawer.*
import kotlinx.android.synthetic.main.nav_header_navigation_drawer.view.*

class NavigationDrawer : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener  {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding
    private var myReceiver: MyReceiver? = null
    private var flag:Boolean = false
    private var mService: LocationUpdatesService? = null


    private var mBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_container)
        val view: View = navView.getHeaderView(0)
        val txtEmail: TextView = view.findViewById(R.id.txtEmailUser)

        if(PrefsApplication.prefs.getData("correo").isNotEmpty()){
            val correo = PrefsApplication.prefs.getData("correo")
            txtEmail.text = correo
        }

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
        myReceiver = MyReceiver()
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
        PrefsApplication.prefs.deleteAll()
        val intent = Intent(this,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    private val service: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder =
                service as LocationUpdatesService.LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    private inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            intent.getParcelableExtra<Location>(LocationUpdatesService.EXTRA_LOCATION)
        }
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences,
        s: String
    ) {

        if (s == StatusLocation.KEY_REQUESTING_LOCATION_UPDATES) {
            if(!flag) {
                setButtonsState(
                    sharedPreferences.getBoolean(
                        StatusLocation.KEY_REQUESTING_LOCATION_UPDATES,
                        false
                    )
                )
            }

        }
    }

    private fun setButtonsState(requestingLocationUpdates: Boolean) {
        if (!requestingLocationUpdates) {
            val alertDialog =
                AlertDialog.Builder(this).create()
            alertDialog.setCancelable(false)
            alertDialog.setMessage("Express Taxi esta accediendo a su ubicación")
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE, "OK"
            ) { dialog, which ->
                dialog.dismiss()
                /*val intent = Intent(this, NavigationDrawer::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)*/
                //finish()


                mService!!.requestLocationUpdates()
            }
            alertDialog.show()
        }
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)

        if(PrefsApplication.prefs.getData("is_service").isNotEmpty()){
            startActivity(Intent(this, DetailsDriverFragment::class.java))
        }else{
            if(mService != null){
                setButtonsState(StatusLocation.requestingLocationUpdates(this))
            }else{
                setButtonsState(false)
            }
        }


// Restore the state of the buttons when the activity (re)launches.
        //System.out.println("onStart" +mService.toString());
        //System.out.println("onStart -> "+ StatusLocation.requestingLocationUpdates(this));


        bindService(
            Intent(this, LocationUpdatesService::class.java), service,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver!!,
            IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
        )
    }
    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver!!)
        super.onPause()
    }

    override fun onStop() {
        if (mBound) {
            unbindService(service)
            mBound = false
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }


}