package com.app.expresstaxi.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.expresstaxi.R
import com.app.expresstaxi.models.Message

class MessageAdapter(val context:Context, val messageList: List<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVED=1;
    val ITEM_SENT=2;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType==1){
            val view:View = LayoutInflater.from(context).inflate(R.layout.received_message_item,parent, false)
            return ReceiveViewHolder(view)
        }else{
            val view:View = LayoutInflater.from(context).inflate(R.layout.sent_message_item,parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.content
        }else{
            val viewHolder = holder as ReceiveViewHolder
            holder.receivedMessage.text = currentMessage.content
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        return if(1==currentMessage.senderId){
            ITEM_SENT
        }else{
            ITEM_RECEIVED
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.txtMessageSent)
    }

    class ReceiveViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val receivedMessage = itemView.findViewById<TextView>(R.id.txtMessageReceived)
    }
}