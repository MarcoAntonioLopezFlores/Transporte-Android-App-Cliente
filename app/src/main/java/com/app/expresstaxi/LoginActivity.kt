package com.app.expresstaxi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.app.expresstaxi.navigation.NavigationDrawer
import com.app.expresstaxi.utils.LocationService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startService(Intent(this, LocationService::class.java))
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnToRegister = findViewById<TextView>(R.id.txtSignup)

        btnToRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }


        btnLogin.setOnClickListener{
            getToken()
            startActivity(Intent(this, NavigationDrawer::class.java))

            finish()
        }
    }

    fun getToken(){
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        FirebaseMessaging.getInstance().token.
        addOnCompleteListener(OnCompleteListener {
            if(!it.isSuccessful){
                println("Error in firebase -> "+it.exception)
                return@OnCompleteListener
            }

            val token = it.result
            println("token -> "+token)
        })
    }

}