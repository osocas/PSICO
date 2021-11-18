package com.uoc.psico.controlador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uoc.psico.R
import com.uoc.psico.modelo.Resenas

class ResenaAdapter(val listaResenas: MutableList<Resenas>): RecyclerView.Adapter<ResenaAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemNombre: TextView
        var itemComentario: TextView
        var itemFecha: TextView
        var itemPuntuacion: RatingBar

        init {
            itemNombre = itemView.findViewById(R.id.id_nombre_resena)
            itemComentario = itemView.findViewById(R.id.tv_resena)
            itemFecha = itemView.findViewById(R.id.tv_fechaResena)
            itemPuntuacion = itemView.findViewById(R.id.rb_itemResena)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_resena_list, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemNombre.text = listaResenas[position].nombre
        holder.itemComentario.text = listaResenas[position].comentario
        holder.itemFecha.text = listaResenas[position].fecha
        holder.itemPuntuacion.rating = listaResenas[position].puntuacion.toFloat()

        //rb_anadir_resena.numStars = 4
        //rb_anadir_resena.rating = 3.5F

    }

    override fun getItemCount(): Int {
        return listaResenas.size
    }

}