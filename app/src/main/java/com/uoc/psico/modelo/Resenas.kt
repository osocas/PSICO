package com.uoc.psico.modelo

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_anadir_resena.*

class Resenas(
    var nombre: String,
    var puntuacion: Double,
    var fecha: String,
    var comentario: String
    //var correoPsicologo: String,
    ){

    private val db = FirebaseFirestore.getInstance()

    fun addResena(correo: String){

        db.collection("resenas").add(
            hashMapOf("correoPsicologo" to correo,
                "nombre" to nombre,
                "puntuacion" to puntuacion,
                "fecha" to fecha,
                "comentario" to comentario)
        )
    }

}