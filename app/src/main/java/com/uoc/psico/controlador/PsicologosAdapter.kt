package com.uoc.psico.controlador

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.uoc.psico.R
import com.uoc.psico.modelo.Psicologos
import kotlinx.android.synthetic.main.activity_perfil.*
import java.text.FieldPosition

//import androidx.recyclerview.widget.RecyclerView

class PsicologosAdapter(val listaPsicologos: MutableList<Psicologos>, val itemClick: (Int) -> Unit): RecyclerView.Adapter<PsicologosAdapter.ViewHolder>() {

    //val nombre = arrayOf("Candela", "Marta", "Carlos", "Raul")
    //val direccion = arrayOf("San Cristobal de la Laguna", "Icod de los vinos", "La Guancha", "Santa Úrsula")
    /*val imagen = arrayOf(
        R.drawable.ic_consejos,
        R.drawable.ic_baseline_forum_24,
        R.drawable.ic_psicologos,
        R.drawable.ic_consejos
    )*/


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){ //, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        var itemImagen: ImageView
        var itemNombre: TextView
        var itemDireccion: TextView

        init {
            itemImagen = itemView.findViewById(R.id.idImagen)
            itemNombre = itemView.findViewById(R.id.idNombre)
            itemDireccion = itemView.findViewById(R.id.idDireccion)


            // para detectar el click de algún elemento
            itemView.setOnClickListener( {itemClick(layoutPosition)} )

        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_psicologos_list, viewGroup, false)
        return ViewHolder(v) //, mListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemNombre.text = listaPsicologos[i].mombre
        viewHolder.itemDireccion.text = listaPsicologos[i].direccion
        //viewHolder.itemImagen.setImageResource(imagen[i])
        Glide.with(viewHolder.itemImagen.context).load(listaPsicologos[i].foto).error(R.drawable.ic_foto_perfil).centerCrop().into(viewHolder.itemImagen)

    }

   // inner class PsicologosViewHolder(itemView:View): BaseV

    override fun getItemCount(): Int {
        return listaPsicologos.size
    }
}