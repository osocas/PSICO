package com.uoc.psico.modelo

import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Foro(
        var correo: String,
        var nombre: String,
        var post: String
) {

        private val db = FirebaseFirestore.getInstance()

        fun addPostDB(date: Date) {
                db.collection("foro").add(
                        hashMapOf("correo" to correo,
                                "nombre" to nombre,
                                "post" to post,
                                "fecha" to date)
                )
        }

}