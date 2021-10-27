package com.app.expresstaxi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.expresstaxi.R
import com.app.expresstaxi.models.Message

class MessageAdapter(context:Context): BaseAdapter() {
    var messages = ArrayList<Message>()
    var context = context

    fun add(message:Message){
        messages.add(message)
    }

    override fun getCount(): Int {
        return messages.size
    }

    override fun getItem(p0: Int): Any {
        return  messages[p0]
    }

    override fun getItemId(p0: Int): Long {
        return  0
    }

    override fun getView(position: Int, view: View?,viewGroup:ViewGroup?): View {
        var holder=MessageViewHolder()
        var myView = view

        var messageInflater = LayoutInflater.from(context)
        var message = messages[position].content
        if(messages[position].idUser.equals("Me")){
            myView = messageInflater.inflate(R.layout.sent_message_item, null)
            holder.textMessage = myView.findViewById(R.id.txtMessage)

            holder.textMessage!!.setText(message)
        }else{
            myView = messageInflater.inflate(R.layout.received_message_item, null)
            holder.textMessage = myView.findViewById(R.id.txtMessage)

            holder.textMessage!!.setText(message)
        }

        return  myView
    }
}

internal class MessageViewHolder{
    var textMessage:TextView?=null
}