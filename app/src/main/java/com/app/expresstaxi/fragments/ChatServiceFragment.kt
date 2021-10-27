package com.app.expresstaxi.fragments

import android.content.*
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.expresstaxi.R
import com.app.expresstaxi.adapters.MessageAdapter
import com.app.expresstaxi.dblocal.ConnectionDB
import com.app.expresstaxi.models.Message
import kotlinx.android.synthetic.main.fragment_chat_service.*

class ChatServiceFragment : AppCompatActivity() {
    private lateinit var messageAdapter:MessageAdapter
    private val FILTER_CHAT = "broadcast_chat"
    companion object{
        var chatActive = false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_service)
        setSupportActionBar(findViewById(R.id.toolbarBackDetails))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat"


        chatActive=true
        messageAdapter = MessageAdapter(this)
        listChat.adapter = messageAdapter

        btnSendMessage.setOnClickListener{
            if(edtMessage.text.toString().trim().isNotEmpty()){
                saveMessage(edtMessage.text.toString())
                sendMessage(edtMessage.text.toString(), "Me")
            }else{
                Toast.makeText(this, "Escribe algún mensaje", Toast.LENGTH_SHORT).show()
            }


        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcast, IntentFilter(FILTER_CHAT))

        getMessages()
    }

    val broadcast = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            sendMessage(intent!!.getStringExtra("mensaje").toString(),"Driver")
        }

    }

    private fun sendMessage(message:String, type:String){
        messageAdapter.add(Message(message,type))
        messageAdapter.notifyDataSetChanged()


        listChat.setSelection(messageAdapter.count-1)
        edtMessage.setText("")

    }

    private fun saveMessage(message:String){
        val con = ConnectionDB(this)
        val data = ContentValues()
        data.put("message",message)
        data.put("user", "Me")
        data.put("seen",1)
        if(con.registerData("chat",data)){
            con.close()
        }

    }

    fun getMessages(){
        try{
            val con = ConnectionDB(this)
            val datos = con.getData("chat",
                arrayOf("id","user","message"))
            while(datos.moveToNext()){
                println(datos.getString(1)+"---------------------"+datos.getString(2))
                messageAdapter.add(Message(datos.getString(2),datos.getString(1)))
            }
            messageAdapter.notifyDataSetChanged()
            listChat.setSelection(messageAdapter.count-1)
            datos.close()

        }catch (e:Exception){

        }
    }

    override fun onStart() {
        super.onStart()
        chatActive=true
    }

    override fun onStop() {
        super.onStop()
        chatActive=false
    }

    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewRoot =inflater.inflate(R.layout.fragment_chat_service, container, false)

        val messageList = ArrayList<Message>()
        viewRoot.recyclerViewChat.layoutManager= LinearLayoutManager(context)
        messageList.add(Message("Hola","A12S3X",1))
        messageList.add(Message("Adios","xaxaxa",2))
        var myAdapterMessage = activity?.let {
            MessageAdapter(it,messageList)
        }

        viewRoot.recyclerViewChat.adapter=myAdapterMessage

        myAdapterMessage!!.notifyDataSetChanged()
        viewRoot.btnSendMessage.setOnClickListener{
            Toast.makeText(context, "Mensaje", Toast.LENGTH_SHORT).show()
        }
        return viewRoot

    }*/


}

