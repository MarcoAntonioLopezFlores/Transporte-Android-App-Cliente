package com.app.expresstaxi.fragments

import android.Manifest

import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng 
import kotlinx.android.synthetic.main.fragment_maps.*
import kotlinx.android.synthetic.main.fragment_maps.view.*
import java.util.*
import android.content.Intent
import android.net.Uri
import com.app.expresstaxi.R
import com.app.expresstaxi.utils.LocationService

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener{
    private lateinit var mMap: GoogleMap
    private lateinit var _latDestination:String
    private lateinit var _longDestination:String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_maps, container, false)
        viewRoot.btnCenterLocalization.setOnClickListener {
            val punto = LatLng(
                LocationService.loc.latitude,
                LocationService.loc.longitude)

            mMap.moveCamera(CameraUpdateFactory.newLatLng(punto))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(punto,16.0f))
        }
        viewRoot.btnSearchDirection.setOnClickListener{
            findLocation(edtAddressToArrive.text.toString())
        }
        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val bottomSheetFragment = BottomSheetFindDriverFragment()


        btnRequestService.setOnClickListener{

            bottomSheetFragment.show(childFragmentManager,"bottomSheetFindDriver")

            //bottomSheetFragment.dismiss()
        }
    }

    private fun findLocation(direccion:String){
        val geocoder = Geocoder(activity, Locale.getDefault())

        val address = geocoder.getFromLocationName(direccion,5)

        println("Lat: "+ address[0].latitude)
        println("Long: "+address[0].longitude)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(address[0].latitude,address[0].longitude)))

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address[0].latitude,address[0].longitude),16.0f))
    }

    private fun findLatLong(latitud:Double, longitud:Double){
        val geocoder = Geocoder(activity, Locale.getDefault())

        val address = geocoder.getFromLocation(latitud,longitud, 5)

        if(address.size>0){
            edtAddressToArrive.setText(address[0].getAddressLine(0))

        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.isMyLocationEnabled=true

        mMap.setOnCameraIdleListener {
            val lat = mMap.cameraPosition.target.latitude
            val long = mMap.cameraPosition.target.longitude
            _latDestination=lat.toString()
            _longDestination=long.toString()
            findLatLong(lat,long)
        }
    }

    fun loadNavigationView(lat: String, lng: String) {
        val navigationIntent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$lat,$lng&mode=d"))
        navigationIntent.setPackage("com.google.android.apps.maps")
        startActivity(navigationIntent)
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }


}