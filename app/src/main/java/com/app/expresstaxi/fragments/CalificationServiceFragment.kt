package com.app.expresstaxi.fragments

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.expresstaxi.R
import com.app.expresstaxi.navigation.NavigationDrawer
import kotlinx.android.synthetic.main.fragment_calification_service.*

class CalificationServiceFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_calification_service)
        setSupportActionBar(findViewById(R.id.toolbarBackDetailsFromCalification))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Calificación del servicio"

        btnSubmitCalification.setOnClickListener{
            val intent = Intent(this, NavigationDrawer::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calification_service, container, false)
    }*/
}