package com.uoc.psico.modelo

import com.google.firebase.firestore.FirebaseFirestore

class Usuario (
    var correo: String,
    var nombre: String,
    var apellidos: String,
    var edad: Int,
    var ciudad: String,
    var psicologo: Boolean)
    //var foto

{

    private val db = FirebaseFirestore.getInstance()

    fun addUsuario(){
        db.collection("usuarios").document(correo).set(
            hashMapOf("nombre" to nombre,
                "apellidos" to apellidos,
                "edad" to edad,
                "ciudad" to ciudad,
                "psicologo" to psicologo,
                "foto" to "")
        )
    }




}


