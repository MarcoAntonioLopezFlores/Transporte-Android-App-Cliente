package com.app.expresstaxi.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.annotation.RequiresApi
import com.app.expresstaxi.R
import com.app.expresstaxi.adapters.ServiceAdapter
import com.app.expresstaxi.models.Servicio
import com.app.expresstaxi.preferences.PrefsApplication
import com.app.expresstaxi.utils.api.APIService
import com.app.expresstaxi.utils.api.RetrofitClient
import kotlinx.android.synthetic.main.fragment_my_services_taken.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class MyServicesTakenFragment : Fragment() {
    private var services:ArrayList<Servicio> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewRoot = inflater.inflate(R.layout.fragment_my_services_taken, container, false)

        fillData(viewRoot)
        viewRoot.listMyServices.setOnItemClickListener { _, _, i, _ ->
            Toast.makeText(context, services[i].conductor!!.usuario.nombre, Toast.LENGTH_SHORT).show()
        }
        return  viewRoot
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun fillData(viewRoot: View){
        val apiService: APIService = RetrofitClient.getAPIService()
        val TOKEN = "Bearer ${PrefsApplication.prefs.getData("token")}"

        apiService.listarServicios(TOKEN, PrefsApplication.prefs.getData("user_id").toLong()).enqueue(object: Callback<List<Servicio>>{
            override fun onResponse(
                call: Call<List<Servicio>>,
                response: Response<List<Servicio>>
            ) {
                if(response.isSuccessful){
                    viewRoot.linear.visibility = View.GONE
                    services = response.body() as ArrayList<Servicio>
                    val myAdapterServices = activity?.let { ServiceAdapter(it,services) }
                    viewRoot.listMyServices.adapter = myAdapterServices
                    myAdapterServices!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Servicio>>, t: Throwable) {
                activity?.let { Toast.makeText(it, "Ocurrió un error, intente de nuevo", Toast.LENGTH_LONG).show() }
            }

        })

//        services.add(Servicio(1, LocalDateTime.now(),4.0,"Marco Lopez", "KAP522ASS"))
//        services.add(Servicio(2, LocalDateTime.now(),4.0,"Marco Lopez", "KAP522ASS"))

    }

}