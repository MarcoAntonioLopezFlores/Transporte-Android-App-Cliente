package com.app.expresstaxi.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.expresstaxi.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_find_driver.view.*

class BottomSheetFindDriverFragment: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_find_driver, container, false)
        view.btnCancelFindDriver.setOnClickListener{
            //findNavController().navigate(R.id.detailsFragment)
            startActivity(Intent(context, DetailsDriverFragment::class.java))
            this.dismiss()

        }

        return view
    }


}