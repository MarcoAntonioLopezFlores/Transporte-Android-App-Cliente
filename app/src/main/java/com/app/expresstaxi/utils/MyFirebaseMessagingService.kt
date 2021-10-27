package com.app.expresstaxi.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.expresstaxi.R
import com.app.expresstaxi.dblocal.ConnectionDB
import com.app.expresstaxi.fragments.ChatServiceFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.data.isNotEmpty()){
            println("Mensaje ->"+remoteMessage.data.toString())

            if(ChatServiceFragment.chatActive){
                sendLocalNotification(remoteMessage.data.get("mensaje").toString())

            }else{
                showNotification(remoteMessage.data.get("mensaje").toString())
                sendLocalNotification(remoteMessage.data.get("mensaje").toString())
                //saveMessage(remoteMessage.data.get("mensaje").toString(),0)
            }

        }
    }

    fun saveMessage(message:String, seen:Int){
        val con = ConnectionDB(this)
        val data = ContentValues()
        data.put("message",message)
        data.put("user", "Driver")
        data.put("seen",seen)
        if(con.registerData("chat",data)){
            con.close()
        }
    }

    fun sendLocalNotification(messageReceived: String){
        val FILTER_CHAT = "broadcast_chat"

        val intent = Intent(FILTER_CHAT)
        intent.putExtra("mensaje", messageReceived)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        saveMessage(messageReceived,1)
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        sendRegistrationToServer(newToken)
    }

    private fun sendRegistrationToServer(token:String){
        println("enviando token al web service -> "+token)
    }

    private fun showNotification(mensaje:String){
        val intent = Intent(this, ChatServiceFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelID = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder=NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.car)
            .setContentTitle("Titulo")
            .setContentText(mensaje)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelID, "title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        //notificationManager.createNotificationChannel()

        notificationManager.notify(0, notificationBuilder.build())
        //saveMessage(mensaje,1)
    }
}