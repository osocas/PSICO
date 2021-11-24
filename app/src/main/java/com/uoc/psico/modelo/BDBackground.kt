package com.uoc.psico.modelo

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.uoc.psico.controlador.ResenaAdapter
import kotlinx.android.synthetic.main.activity_info_psicologo.*

class BDBackground  {
    /*private val db = FirebaseFirestore.getInstance()

    private val _listaPsicologosLD = MutableLiveData<MutableList<Psicologos>>()
    val listaPsicologosLD: LiveData<MutableList<Psicologos>> get() = _listaPsicologosLD

    fun getListaPsicologos() {
        var listaPsicologos = mutableListOf<Psicologos>()

        db.collection("psicologos").get().addOnSuccessListener{ result ->

            for (i in result){
                listaPsicologos.add(Psicologos(i.data.get("correo") as String, i.data.get("mombre") as String,
                    i.data.get("direccion") as String, i.data.get("precio") as String, (i.data.get("n_telefono") as Number).toInt(),
                    i.data.get("especialidades") as String, i.data.get("horario") as String, i.data.get("consulta_online") as Boolean,
                    i.data.get("consulta_presencial") as Boolean, i.data.get("consulta_telefonica") as Boolean,
                    i.data.get("foto") as String, i.data.get("descripcion") as String, (i.data.get("puntuacion_media") as Number).toDouble()))

            }
            Log.d("TAG", "CONTIENE: " + listaPsicologos[0].correo)
            _listaPsicologosLD.value = listaPsicologos
        }

    }*/

}