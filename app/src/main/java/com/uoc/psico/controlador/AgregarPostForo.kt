package com.uoc.psico.controlador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.modelo.Foro
import kotlinx.android.synthetic.main.activity_agregar_post_foro.*

class AgregarPostForo : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_post_foro)


        botonAnadirPostForo()

    }


    fun botonAnadirPostForo(){
        bt_agregarForo.setOnClickListener{
            if (user != null) {
                db.collection("usuarios").document(user.email.toString()).get().addOnSuccessListener {
                    Log.d("TAG", "el nombre: " + it.get("nombre") as String? + " " + it.get("apellidos") as String?)
                    Foro(user.email.toString(), it.get("nombre") as String? + " " + it.get("apellidos") as String?, et_agregarForo.text.toString()).addPostDB()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("seleccionado", "foroFragment")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(intent)

                }
            }
        }
    }

}