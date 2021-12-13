package com.app.expresstaxi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.app.expresstaxi.models.*
import com.app.expresstaxi.utils.api.APIFirebase
import com.app.expresstaxi.utils.api.APIService
import com.app.expresstaxi.utils.api.RetrofitClient
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnCancel = findViewById<Button>(R.id.btnCancelRegister)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        btnContinue.setOnClickListener {
            if(edtName.text!!.isEmpty() || edtLastname.text!!.isEmpty() || edtEmail.text!!.isEmpty() || edtPassword.text!!.isEmpty() || edtPasswordConfirm.text!!.isEmpty()){
                Toast.makeText(this, "Los campos marcados con asteriscos son obligatorios", Toast.LENGTH_LONG).show()
            }else{
                if(edtPassword.text.toString() != edtPasswordConfirm.text.toString()){
                    Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_LONG).show()
                }else{
                    val apiService: APIService = RetrofitClient.getAPIService()
                    val rol = Rol(null,"","")
                    val lastName = edtLastname.text.toString().split(" ")
                    var apellidoP = ""; var apellidoM = ""
                    if(lastName.size > 1){
                        apellidoP = lastName[0]
                        apellidoM = lastName[1]
                    }else{
                        apellidoP = lastName[0]
                    }
                    val usuario = Usuario(null, edtPassword.text.toString(), edtPhone.text.toString(), edtEmail.text.toString(), edtName.text.toString(), apellidoP, apellidoM, "", true, rol, null)
                    val localizacion = Localizacion(null, 0.0, 0.0)

                    apiService.registrarCliente(Cliente(null, null, usuario, localizacion)).enqueue(object: Callback<Cliente>{
                        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                            if(response.isSuccessful){
                                registrar(response.body() as Cliente)
                            }
                        }

                        override fun onFailure(call: Call<Cliente>, t: Throwable) {
                            mostrarMensaje()
                        }
                    })
                }
            }
        }

        btnCancel.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registrar(cliente: Cliente){
        if(cliente.id as Long > 0){
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun mostrarMensaje(){
        Toast.makeText(this, "Ocurrió un error, intente de nuevo", Toast.LENGTH_LONG).show()
    }

}