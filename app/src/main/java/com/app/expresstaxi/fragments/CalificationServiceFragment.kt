package com.app.expresstaxi.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.expresstaxi.R
import com.app.expresstaxi.models.Estado
import com.app.expresstaxi.models.Servicio
import com.app.expresstaxi.navigation.NavigationDrawer
import com.app.expresstaxi.preferences.PrefsApplication
import com.app.expresstaxi.utils.api.APIService
import com.app.expresstaxi.utils.api.RetrofitClient
import kotlinx.android.synthetic.main.fragment_calification_service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalificationServiceFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_calification_service)
        setSupportActionBar(findViewById(R.id.toolbarBackDetailsFromCalification))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Calificación del servicio"

        btnSubmitCalification.setOnClickListener{
            calificar(ratingBarCalificationService.rating)
        }
    }

    fun calificar(calificacion: Float){
        val apiService: APIService = RetrofitClient.getAPIService()
        val TOKEN = "Bearer ${PrefsApplication.prefs.getData("token")}"
        val servicio = Servicio(PrefsApplication.prefs.getData("servicio_id").toLong(), null, calificacion, 0.0,0.0,0.0,0.0, null , null, null)

        apiService.calificar(TOKEN, servicio).enqueue(object: Callback<Servicio>{
            override fun onResponse(call: Call<Servicio>, response: Response<Servicio>) {
                if(response.isSuccessful){
                    finalizar()
                }
            }

            override fun onFailure(call: Call<Servicio>, t: Throwable) {
                mostrarMensaje()
            }

        })
    }

    fun finalizar(){
        val intent = Intent(this, NavigationDrawer::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        PrefsApplication.prefs.delete("is_service")
        PrefsApplication.prefs.delete("servicio_id")
        PrefsApplication.prefs.delete("avance")
        startActivity(intent)
        finish()
    }

    fun mostrarMensaje(){
        Toast.makeText(this, "Ocurrió un error, intente de nuevo", Toast.LENGTH_LONG).show()
    }
    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calification_service, container, false)
    }*/
}