package com.app.expresstaxi.fragments

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context

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
import android.content.IntentFilter
import android.net.Uri
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.expresstaxi.R
import com.app.expresstaxi.models.*
import com.app.expresstaxi.preferences.PrefsApplication
import com.app.expresstaxi.utils.LocationService
import com.app.expresstaxi.utils.api.APIFirebase
import com.app.expresstaxi.utils.api.APIService
import com.app.expresstaxi.utils.api.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener{
    private lateinit var mMap: GoogleMap
    private lateinit var _latDestination:String
    private lateinit var _longDestination:String
    private var latOrigin: Double = 0.0
    private var longOrigin: Double = 0.0
    private var latArrive: Double = 0.0
    private var longArrive: Double = 0.0
    private var isLocationOriginConfirm = false
    private var isLocationToArriveConfirm= false
    private val KEY = "AAAADYblWbE:APA91bF3zj6eBR1Hbl75OTVMd_k7dnR4znuw2BiNxY0iKrKRrP0ZNxnlDevqSbWeAdYmoyU-KJ8F3CKuFEB6CeDykvzDNe_P_JByhLl792zh40pcZXYzL--uPoJrSOI8MtdpUKcECVK2"
    private val FILTRO_CHAT = "broadcast_confirmacion"

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

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcast, IntentFilter(FILTRO_CHAT))
        return viewRoot
    }

    val broadcast = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent != null){
                PrefsApplication.prefs.save("is_service", "true")
                //startActivity(Intent(context, DetailsDriverFragment::class.java))
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        val bottomSheetFragment = BottomSheetFindDriverFragment()

        btnSearchDirectionOrigin.setOnClickListener{
            if(edtAddressOrigin.text.toString().trim().isNotEmpty()){
                findLocation(edtAddressOrigin.text.toString(), "Origin")
            }else{
                Toast.makeText(context, "Es necesario ingresar una dirección", Toast.LENGTH_SHORT).show()
            }
        }
        btnSearchDirectionToArrive.setOnClickListener{
            if(edtAddressToArrive.text.toString().trim().isNotEmpty()){
                findLocation(edtAddressToArrive.text.toString(), "Arrive")
            }else{
                Toast.makeText(context, "Es necesario ingresar una dirección", Toast.LENGTH_SHORT).show()
            }

        }
        btnConfirmDirection.setOnClickListener {
            if(!isLocationOriginConfirm){
                isLocationOriginConfirm = true
                ll_destination_place.visibility = View.VISIBLE
                btnCancelDirectionOrigin.visibility = View.VISIBLE
                btnSearchDirectionOrigin.visibility = View.GONE
                edtAddressOrigin.isEnabled = false
//                findLocation(edtAddressOrigin.text.toString(), "Origin")
            }else{
                isLocationToArriveConfirm = true
                btnConfirmDirection.visibility = View.GONE
                btnRequestService.visibility = View.VISIBLE
                btnCancelDirectionToArrive.visibility = View.VISIBLE
                btnSearchDirectionToArrive.visibility = View.GONE
                edtAddressToArrive.isEnabled = false
//                findLocation(edtAddressToArrive.text.toString(), "Arrive")
            }
        }

        btnCancelDirectionOrigin.setOnClickListener{
            ll_destination_place.visibility = View.GONE
            btnCancelDirectionOrigin.visibility = View.GONE
            btnSearchDirectionOrigin.visibility = View.VISIBLE
            isLocationOriginConfirm = false
            edtAddressOrigin.isEnabled = true
        }

        btnCancelDirectionToArrive.setOnClickListener{
            isLocationToArriveConfirm = false
            btnConfirmDirection.visibility = View.VISIBLE
            btnRequestService.visibility = View.GONE
            btnCancelDirectionToArrive.visibility = View.GONE
            btnSearchDirectionToArrive.visibility = View.VISIBLE
            edtAddressToArrive.isEnabled = true
        }

        btnRequestService.setOnClickListener {
            if(edtAddressOrigin.text.toString().trim().isNotEmpty() && edtAddressToArrive.text.toString().trim().isNotEmpty()){
                val apiService: APIService = RetrofitClient.getAPIService()

                val idUsuario = PrefsApplication.prefs.getData("user_id").toLong()
                val correo = PrefsApplication.prefs.getData("correo")
                val estado = Estado(null, "Solicitado")
                val usuario = Usuario(idUsuario, "","",correo, "","","","",true, Rol(null, "",""))
                val localizacion = Localizacion(null, 0.0, 0.0)
                val client = Cliente(null, null, usuario, localizacion)
                val servicio = Servicio(null, null, null, latOrigin, longOrigin, latArrive, longArrive, estado, client, null)
                val TOKEN = "Bearer ${PrefsApplication.prefs.getData("token")}"

                apiService.registrarServicio(TOKEN, servicio).enqueue(object : Callback<Servicio>{
                    override fun onResponse(call: Call<Servicio>, response: Response<Servicio>) {
                        if(response.isSuccessful){
                            val servicio = response.body() as Servicio
                            PrefsApplication.prefs.save("servicio_id", servicio.id.toString())
                            enviarNotificacion(servicio)
                            bottomSheetFragment.show(childFragmentManager, "bottomSheetFindDriver")
                        }
                    }

                    override fun onFailure(call: Call<Servicio>, t: Throwable) {
                        mostrarMensaje()
                    }
                })
            }else{
                Toast.makeText(context, "Es necesario ingresar 2 direcciones para solicitar un servicio", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun findLocation(direccion:String, tipo:String){
        try{
            val geocoder = Geocoder(activity, Locale.getDefault())

            val address = geocoder.getFromLocationName(direccion,5)

            if(tipo == "Origin"){
                latOrigin = address[0].latitude
                longOrigin = address[0].longitude
            }else{
                latArrive = address[0].latitude
                longArrive = address[0].longitude
            }

            println("Lat: "+ address[0].latitude)
            println("Long: "+address[0].longitude)

            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(address[0].latitude,address[0].longitude)))

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address[0].latitude,address[0].longitude),16.0f))
        }catch (e:Exception){
            Toast.makeText(context, "Es necesario ingresar una dirección valida", Toast.LENGTH_SHORT).show()
        }

    }

    private fun findLatLong(latitud:Double, longitud:Double){
        val geocoder = Geocoder(activity, Locale.getDefault())

        val address = geocoder.getFromLocation(latitud,longitud, 5)

        if(address.size>0){
            if(isLocationOriginConfirm){
                edtAddressToArrive.setText(address[0].getAddressLine(0))
            }else{
                edtAddressOrigin.setText(address[0].getAddressLine(0))
            }
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
            if(edtAddressToArrive.isEnabled){
                val lat = mMap.cameraPosition.target.latitude
                val long = mMap.cameraPosition.target.longitude
                _latDestination=lat.toString()
                _longDestination=long.toString()
                findLatLong(lat,long)
            }

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

    private fun mostrarMensaje(){
        Toast.makeText(context, "Ocurrió un error, intente de nuevo", Toast.LENGTH_LONG).show()
    }

    private fun enviarNotificacion(servicio: Servicio){
        val apiFirebase: APIFirebase = RetrofitClient.getAPIFirebase()
        val notificacion = Notificacion(PrefsApplication.prefs.getData("tokenconductorfb"), Datos(servicio.id!!.toString(), "Servicio","Solicitando servicio","Ingrese para aceptar el servicio"))

        apiFirebase.enviarNotificacion("key=$KEY", notificacion).enqueue(object: Callback<JSONObject>{
            override fun onResponse(
                call: Call<JSONObject>,
                response: Response<JSONObject>
            ) {
                if(response.isSuccessful){
                    println("Se envió la notificación")
                }
            }

            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                mostrarMensaje()
            }
        })
    }

}