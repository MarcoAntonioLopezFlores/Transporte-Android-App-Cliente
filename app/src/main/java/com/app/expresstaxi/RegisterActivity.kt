package com.app.expresstaxi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnCancel = findViewById<Button>(R.id.btnCancelRegister)
        val btnContinue = findViewById<Button>(R.id.btnContinue)

        btnContinue.setOnClickListener {
            Toast.makeText(this, "Registrar", Toast.LENGTH_SHORT).show()
        }

        btnCancel.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }




}