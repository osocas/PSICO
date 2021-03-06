package com.uoc.psico.controlador.foro

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.uoc.psico.R
import com.uoc.psico.modelo.Foro

class ForoAdapter(val listaPosts: MutableList<Foro>, val itemClick: (Int) -> Unit): RecyclerView.Adapter<ForoAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemNombre: TextView
        var itemPost: TextView


        init {
            itemNombre = itemView.findViewById(R.id.id_nombre_foro)
            itemPost = itemView.findViewById(R.id.id_post_foro)



            itemView.setOnClickListener( {itemClick(layoutPosition)} )

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_foro_list, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemNombre.text = listaPosts[i].nombre
        viewHolder.itemPost.text = listaPosts[i].post


    }

    override fun getItemCount(): Int {
        return listaPosts.size
    }

}