package com.uoc.psico.modelo

import com.google.firebase.firestore.FirebaseFirestore

class Foro(var correo: String,
        var nombre: String,
        var post: String) {

        private val db = FirebaseFirestore.getInstance()

        fun addPostDB(){
                db.collection("foro").add(
                        hashMapOf("correo" to correo,
                                "nombre" to nombre,
                                "post" to post)
                )
        }

}