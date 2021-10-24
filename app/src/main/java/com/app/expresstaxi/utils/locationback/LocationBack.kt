package com.app.expresstaxi.utils.locationback

import android.content.*
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.expresstaxi.LoginActivity
import com.app.expresstaxi.R
import com.app.expresstaxi.navigation.NavigationDrawer


class LocationBack : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private var myReceiver: MyReceiver? = null
    private var flag:Boolean = false
    private var mService: LocationUpdatesService? = null


    private var mBound = false
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_navigation_drawer)

        myReceiver = MyReceiver()

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
                val intent = Intent(this, NavigationDrawer::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()


                mService!!.requestLocationUpdates()
            }
            alertDialog.show()
        }
    }

    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)


// Restore the state of the buttons when the activity (re)launches.
        //System.out.println("onStart" +mService.toString());
        //System.out.println("onStart -> "+ StatusLocation.requestingLocationUpdates(this));


        if(mService != null){
            setButtonsState(StatusLocation.requestingLocationUpdates(this))
        }else{
            setButtonsState(false);
        }

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