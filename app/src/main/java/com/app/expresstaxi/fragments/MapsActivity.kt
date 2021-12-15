package com.app.expresstaxi.fragments

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.app.expresstaxi.R
import com.app.expresstaxi.models.Localizacion
import com.app.expresstaxi.models.Servicio
import com.app.expresstaxi.preferences.PrefsApplication
import com.app.expresstaxi.utils.api.APIService
import com.app.expresstaxi.utils.api.RetrofitClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        setSupportActionBar(findViewById(R.id.toolbarBackDetails))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Posición del conductor"
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapConductor) as SupportMapFragment
        mapFragment.getMapAsync(this)

        println("entro a buscar latitud")
        //obtenerLocalizacionConductor()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.post(object : Runnable {
            override fun run() {
                mMap.clear()
                println("llamada a latitud")
                obtenerLocalizacionConductor()
                myHandler.postDelayed(this, 5000 )
            }
        })

    }

    fun obtenerLocalizacionConductor() {
        val apiService: APIService = RetrofitClient.getAPIService()
        try{
            val id = PrefsApplication.prefs.getData("servicio_id").toLong()
            val TOKEN = "Bearer ${PrefsApplication.prefs.getData("token")}"
            println("latitud id service:"+id)
            CoroutineScope(Dispatchers.IO).launch {
                apiService.obtenerUbicacionConductor(TOKEN, id).enqueue(object: Callback<Localizacion> {
                    override fun onResponse(call: Call<Localizacion>, response: Response<Localizacion>) {
                        runOnUiThread {
                            if(response.isSuccessful){
                                var localizacion = response.body() as Localizacion
                                println("latitud conductor obtenida: "+localizacion.latitud)
                                var punto = LatLng(localizacion.latitud,
                                    localizacion.longitud)

                                mMap.addMarker(MarkerOptions().position(punto).title("Conductor"))
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(punto))
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(punto,16.0f))
                            }
                        }
                    }

                    override fun onFailure(call: Call<Localizacion>, t: Throwable) {
                        println("latitud conductor no obtenida")
                        //Toast.makeText(applicationContext, "Algo salio mal", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }catch (e:Exception){

        }

    }

}