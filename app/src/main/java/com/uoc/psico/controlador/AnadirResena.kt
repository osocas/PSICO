package com.uoc.psico.controlador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import kotlinx.android.synthetic.main.activity_anadir_resena.*
import kotlinx.android.synthetic.main.activity_perfil.*
import java.text.SimpleDateFormat
import java.util.*

class AnadirResena : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_resena)


        val extras = intent.extras
        val correo = extras?.getString("correo")

        //rb_anadir_resena.numStars = 4
        //rb_anadir_resena.rating = 3.5F


        bt_compartir_resena.setOnClickListener{


            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            //Log.d("TAG", "el ratinbar: " + rb_anadir_resena.rating.toDouble() + " la fecha es: " + currentDate)



            if (user != null) {

                var nombre = ""
                db.collection("usuarios").document(user.email.toString()).get().addOnSuccessListener {

                    db.collection("resenas").add(
                        hashMapOf("correoPsicologo" to correo,
                            "nombre" to (it.get("nombre") as String? + " " + it.get("apellidos") as String?),
                            "puntuacion" to rb_anadir_resena.rating.toDouble(),
                            "fecha" to currentDate,
                            "comentario" to et_anadir_resena.text.toString())
                    )

                }




            }


        }


    }
}