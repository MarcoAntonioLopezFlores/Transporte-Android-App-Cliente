package com.app.expresstaxi.fragments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.expresstaxi.R
import com.app.expresstaxi.navigation.NavigationDrawer
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import kotlinx.android.synthetic.main.fragment_details_driver.*



class DetailsDriverFragment:AppCompatActivity() {
    private val FILTER_CHAT = "broadcast_chat"
    var counterMessages = 1
    lateinit var badgeDrawable: BadgeDrawable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_details_driver)
        setSupportActionBar(findViewById(R.id.toolbarBackHome))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalles del servicio"

        badgeDrawable = BadgeDrawable.create(this)

        btnChatService.setOnClickListener{
            startActivity(Intent(this,ChatServiceFragment::class.java))
            counterMessages=1
            badgeDrawable.isVisible=false
        }

        btnStartService.setOnClickListener{
            btnStartService.visibility = View.GONE
            btnFinishService.visibility = View.VISIBLE
            btnCancelService.visibility = View.GONE
            containerBtnChatService.visibility = View.VISIBLE
        }

        btnCancelService.setOnClickListener{
            startActivity(Intent(this, NavigationDrawer::class.java))
            finish()

        }

        btnFinishService.setOnClickListener{
            startActivity(Intent(this, CalificationServiceFragment::class.java))
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcast, IntentFilter(FILTER_CHAT))

    }

    private val broadcast= object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            setCounter(btnChatService, counterMessages)
            counterMessages++
        }

    }

    @SuppressLint("UnsafeOptInUsageError")
    fun setCounter(view: View, i:Int){
        badgeDrawable.number = i
        badgeDrawable.isVisible = true
        BadgeUtils.attachBadgeDrawable(badgeDrawable, view)
    }

    /*override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_details_driver, container, false)

        viewRoot.btnChatService.setOnClickListener{
            findNavController().navigate(R.id.action_detailsFragment_to_chatServiceFragment)
        }

        viewRoot.btnStartService.setOnClickListener{
            viewRoot.btnStartService.visibility = View.GONE
            viewRoot.btnFinishService.visibility = View.VISIBLE
            viewRoot.btnCancelService.visibility = View.GONE
            viewRoot.containerBtnChatService.visibility = View.VISIBLE
        }

        viewRoot.btnCancelService.setOnClickListener{

            findNavController().navigate(R.id.action_detailsFragment_to_mapsFragment)

        }

        viewRoot.btnFinishService.setOnClickListener{
            findNavController().navigate(R.id.action_detailsFragment_to_calificationServiceFragment)

        }

        return viewRoot
    }*/


}