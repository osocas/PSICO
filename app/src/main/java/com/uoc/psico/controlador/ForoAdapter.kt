package com.uoc.psico.controlador

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.uoc.psico.R

class ForoAdapter: RecyclerView.Adapter<ForoAdapter.ViewHolder>() {

    val nombre = arrayOf("Candela", "Marta", "Carlos", "Raul")
    val post = arrayOf("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
        "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
        "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.",
        "The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. ")

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemNombre: TextView
        var itemPost: TextView

        init {
            itemNombre = itemView.findViewById(R.id.id_nombre_foro)
            itemPost = itemView.findViewById(R.id.id_post_foro)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_foro_list, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemNombre.text = nombre[i]
        viewHolder.itemPost.text = post[i]

    }

    override fun getItemCount(): Int {
        return nombre.size
    }

}