package com.app.expresstaxi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.expresstaxi.R
import com.app.expresstaxi.models.*
import com.app.expresstaxi.preferences.PrefsApplication
import com.app.expresstaxi.utils.api.APIFirebase
import com.app.expresstaxi.utils.api.APIService
import com.app.expresstaxi.utils.api.RetrofitClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_find_driver.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BottomSheetFindDriverFragment: BottomSheetDialogFragment() {

    private val KEY = "AAAADYblWbE:APA91bF3zj6eBR1Hbl75OTVMd_k7dnR4znuw2BiNxY0iKrKRrP0ZNxnlDevqSbWeAdYmoyU-KJ8F3CKuFEB6CeDykvzDNe_P_JByhLl792zh40pcZXYzL--uPoJrSOI8MtdpUKcECVK2"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_find_driver, container, false)
        view.btnCancelFindDriver.setOnClickListener{
            cambiarEstado("Cancelado")
        }

        return view
    }

    fun cambiarEstado(estado: String){
        val apiService: APIService = RetrofitClient.getAPIService()
        val servicio = Servicio(
            PrefsApplication.prefs.getData("servicio_id").toLong(), null, 0f, 0.0,0.0,0.0,0.0,
            Estado(null, estado), null, null)
        val TOKEN = "Bearer ${PrefsApplication.prefs.getData("token")}"

        apiService.cambiarEstado(TOKEN, servicio).enqueue(object: Callback<Servicio> {
            override fun onResponse(call: Call<Servicio>, response: Response<Servicio>) {
                if(response.isSuccessful){
                    when (estado) {
                        "Cancelado" -> {
                            notificar("Cancelar","Actualización del viaje","El cliente ha cancelado el viaje")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<Servicio>, t: Throwable) {
                println("No se cambió el estado")
            }

        })
    }

    fun notificar(tipo: String, title: String, body: String){
        val apiFirebase: APIFirebase = RetrofitClient.getAPIFirebase()
        val notificacion = Notificacion(PrefsApplication.prefs.getData("tokenconductorfb"), Datos(PrefsApplication.prefs.getData("servicio_id"), tipo,title,body))

        apiFirebase.enviarNotificacion("key=$KEY", notificacion).enqueue(object: Callback<JSONObject>{
            override fun onResponse(
                call: Call<JSONObject>,
                response: Response<JSONObject>
            ) {
                if(response.isSuccessful){
                    when (tipo) {
                        "Cancelar" -> {
                            cancelar()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                mostrarMensaje()
            }
        })
    }

    fun cancelar(){
        this.dismiss()
        PrefsApplication.prefs.delete("is_service")
        PrefsApplication.prefs.delete("servicio_id")
        PrefsApplication.prefs.delete("avance")
    }

    private fun mostrarMensaje(){
        Toast.makeText(context, "Ocurrió un error, intente de nuevo", Toast.LENGTH_LONG).show()
    }
}