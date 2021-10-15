package com.app.expresstaxi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.expresstaxi.R
import com.app.expresstaxi.adapters.ServiceAdapter
import com.app.expresstaxi.models.ServiceTrip
import kotlinx.android.synthetic.main.fragment_my_services_taken.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MyServicesTakenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewRoot = inflater.inflate(R.layout.fragment_my_services_taken, container, false)

        fillData(viewRoot)

        return  viewRoot
    }

    fun fillData(viewRoot: View){
        var services:ArrayList<ServiceTrip> = ArrayList<ServiceTrip>()

        services.add(ServiceTrip(1, LocalDateTime.now(),4.0,"Marco Lopez", "KAP522ASS"))

        var myAdapterServices = activity?.let {
            ServiceAdapter(services, it)
        }

        viewRoot.listMyServices.adapter = myAdapterServices

        myAdapterServices!!.notifyDataSetChanged()

    }

}