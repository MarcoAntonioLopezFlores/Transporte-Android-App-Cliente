package com.app.expresstaxi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.expresstaxi.adapters.ServiceAdapter
import com.app.expresstaxi.models.Servicio
import com.app.expresstaxi.models.Usuario
import com.app.expresstaxi.preferences.PrefsApplication
import com.app.expresstaxi.utils.api.APIService
import com.app.expresstaxi.utils.api.RetrofitClient
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.android.synthetic.main.fragment_my_profile.view.*
import kotlinx.android.synthetic.main.fragment_my_services_taken.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)
        //@GetMapping("/buscar/{id}")
        println(PrefsApplication.prefs.getData("correo").isNotEmpty())
        println(PrefsApplication.prefs.getData("user_id").isNotEmpty())

        val apiService: APIService = RetrofitClient.getAPIService()
        val TOKEN = "Bearer ${PrefsApplication.prefs.getData("token")}"

        apiService.buscarUsuario(TOKEN, PrefsApplication.prefs.getData("user_id").toLong()).enqueue(object:
            Callback<Usuario> {
            override fun onResponse(
                call: Call<Usuario>,
                response: Response<Usuario>
            ) {
                if(response.isSuccessful){
                    val usuario =response.body() as Usuario
                    view.txtNameProfile.text = usuario.nombre
                    view.txtLastnameProfile.text = usuario.apellidoPaterno + " " + usuario.apellidoMaterno
                    view.txtTelephoneProfile.text = usuario.telefono
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                activity?.let { Toast.makeText(it, "Ocurrió un error, intente de nuevo", Toast.LENGTH_LONG).show() }
            }

        })

        if(PrefsApplication.prefs.getData("correo").isNotEmpty()){
            val correo = PrefsApplication.prefs.getData("correo")
            view.txtEmailProfile.text = correo
        }
        return view
    }

}