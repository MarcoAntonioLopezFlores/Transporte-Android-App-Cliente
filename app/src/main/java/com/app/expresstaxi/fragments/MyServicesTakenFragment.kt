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
import com.app.expresstaxi.models.ServiceTrip
import kotlinx.android.synthetic.main.fragment_my_services_taken.view.*
import java.time.LocalDateTime
import kotlin.collections.ArrayList

class MyServicesTakenFragment : Fragment() {
    private val services:ArrayList<ServiceTrip> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewRoot = inflater.inflate(R.layout.fragment_my_services_taken, container, false)

        fillData(viewRoot)
        viewRoot.listMyServices.setOnItemClickListener { _, _, i, _ ->
            Toast.makeText(context, services[i].nameDriver, Toast.LENGTH_SHORT).show()
        }
        return  viewRoot
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun fillData(viewRoot: View){

        services.add(ServiceTrip(1, LocalDateTime.now(),4.0,"Marco Lopez", "KAP522ASS"))
        services.add(ServiceTrip(2, LocalDateTime.now(),4.0,"Marco Lopez", "KAP522ASS"))

        viewRoot.linear.visibility = View.GONE
        val myAdapterServices = activity?.let { ServiceAdapter(it,services) }
        viewRoot.listMyServices.adapter = myAdapterServices


        myAdapterServices!!.notifyDataSetChanged()

    }

}