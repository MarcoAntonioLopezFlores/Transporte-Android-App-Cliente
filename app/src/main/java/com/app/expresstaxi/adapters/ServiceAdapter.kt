package com.app.expresstaxi.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.app.expresstaxi.R
import com.app.expresstaxi.models.Servicio
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ServiceAdapter(context: Context, items: List<Servicio>) :
    ArrayAdapter<Servicio>(context, R.layout.item_service, items){

    private  class AdapterListHolder{
        var date : TextView? = null
        var ratingService: MaterialRatingBar? = null
        var nameDriver: TextView? = null
        var autoIdDriver:TextView? = null
        var estado:TextView? = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: AdapterListHolder
        if(view == null){

            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.item_service, parent, false)
            viewHolder = AdapterListHolder()

            viewHolder.nameDriver = view.findViewById(R.id.txtDriverNameService) as TextView
            viewHolder.autoIdDriver = view.findViewById(R.id.txtDriverAutoIdService) as TextView
            viewHolder.ratingService = view.findViewById(R.id.txtCalificationService) as MaterialRatingBar
            viewHolder.date = view.findViewById(R.id.txtServiceDate) as TextView
            viewHolder.estado = view.findViewById(R.id.textEstado) as TextView

        }else{
            viewHolder = view.tag as AdapterListHolder
        }
        val elemento = getItem(position)
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val formatted = format.format(elemento!!.fechaRegistro)

        viewHolder.nameDriver!!.text = if(elemento.conductor != null) elemento.conductor.usuario.nombre else ""
        viewHolder.autoIdDriver!!.text = if(elemento.conductor != null)  elemento.conductor.vehiculo.placas else "Sin placas"
        viewHolder.date!!.text = formatted
        viewHolder.ratingService!!.rating = elemento.calificacion!!
        viewHolder.estado!!.text = elemento.estado!!.descripcion

        view!!.tag = viewHolder
        return view
    }


}