package com.uoc.psico.controlador

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.uoc.psico.R

class ConsejosAdapter: RecyclerView.Adapter<ConsejosAdapter.ViewHolder>() {

    val titulo = arrayOf("Higiene del sueño", "Hábitos saludables", "Psicología positiva", "Relajación Schultz", "Relajación muscular", "Inteligencia emocional")

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemTitulo: TextView


        init {
            itemTitulo = itemView.findViewById(R.id.id_consejo_list)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_consejos_list, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitulo.text = titulo[i]

    }

    override fun getItemCount(): Int {
        return titulo.size
    }




}