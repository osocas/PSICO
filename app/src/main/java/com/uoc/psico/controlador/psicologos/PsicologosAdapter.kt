package com.uoc.psico.controlador.psicologos

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.uoc.psico.R
import com.uoc.psico.modelo.Psicologos


class PsicologosAdapter(val listaPsicologos: MutableList<Psicologos>, val itemClick: (Int) -> Unit): RecyclerView.Adapter<PsicologosAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImagen: ImageView
        var itemNombre: TextView
        var itemDireccion: TextView
        var itemPuntuacion: RatingBar
        var itemNoHayResenas: TextView

        init {
            itemImagen = itemView.findViewById(R.id.idImagen)
            itemNombre = itemView.findViewById(R.id.idNombre)
            itemDireccion = itemView.findViewById(R.id.idDireccion)
            itemPuntuacion = itemView.findViewById(R.id.rb_itemPsicologo)
            itemNoHayResenas = itemView.findViewById(R.id.tv_no_hay_resenas)


            // para detectar el click de algún elemento
            itemView.setOnClickListener( {itemClick(layoutPosition)} )

        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_psicologos_list, viewGroup, false)
        return ViewHolder(v) //, mListener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemNombre.text = listaPsicologos[i].nombre

        //Mostramos la ciudad o municipio con la primera letra de cada palabra en mayúscula.
        val list = listaPsicologos[i].ciudad?.split(" ")
        var newCiudad= ""

        if (list != null) {
            for(i in list){
                newCiudad += i.toUpperCase()[0].toString() + i.substring(1, i.length).toLowerCase() + " "
            }
        }

        if(listaPsicologos[i].ciudad.toLowerCase() == listaPsicologos[i].provincia.toLowerCase()){
            viewHolder.itemDireccion.text = listaPsicologos[i].direccion + ", " + newCiudad + "."
        }else{
            viewHolder.itemDireccion.text = listaPsicologos[i].direccion + ", " + newCiudad +  ", " + listaPsicologos[i].provincia + "."
        }

        Glide.with(viewHolder.itemImagen.context).load(listaPsicologos[i].foto).error(R.drawable.ic_no_foto).centerCrop().into(viewHolder.itemImagen)

        if (listaPsicologos[i].puntuacion_media.toString() != "NaN" && listaPsicologos[i].puntuacion_media != 99.0){
            viewHolder.itemPuntuacion.setVisibility(RatingBar.VISIBLE)
            viewHolder.itemNoHayResenas.setVisibility(TextView.INVISIBLE)
            viewHolder.itemPuntuacion.rating = (listaPsicologos[i].puntuacion_media.toFloat())
        }

    }


    override fun getItemCount(): Int {
        return listaPsicologos.size
    }
}