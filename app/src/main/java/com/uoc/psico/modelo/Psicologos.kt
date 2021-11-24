package com.uoc.psico.modelo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_publicitarse.*

class Psicologos(
        var correo: String,
        var mombre: String,
        var direccion: String,
        var precio: String,
        var n_telefono: Int,
        var especialidades: String,
        var horario: String,
        var consulta_online: Boolean,
        var consulta_presencial: Boolean,
        var consulta_telefonica: Boolean,
        var foto: String,
        var descripcion: String,
        var puntuacion_media: Double) {

        private val db = FirebaseFirestore.getInstance()

        fun addPsicologo(){
                db.collection("psicologos").document(correo).set(
                        hashMapOf("correo" to correo,
                                "mombre" to mombre,
                                "direccion" to direccion,
                                "precio" to precio,
                                "n_telefono" to n_telefono,
                                "especialidades" to especialidades,
                                "horario" to horario,
                                "consulta_online" to consulta_online,
                                "consulta_presencial" to consulta_presencial,
                                "consulta_telefonica" to consulta_telefonica,
                                "foto" to foto,
                                "descripcion" to descripcion,
                                "puntuacion_media" to puntuacion_media))
        }





}