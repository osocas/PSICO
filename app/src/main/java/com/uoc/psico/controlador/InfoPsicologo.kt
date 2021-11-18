package com.uoc.psico.controlador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.modelo.Psicologos
import com.uoc.psico.modelo.Resenas
import kotlinx.android.synthetic.main.activity_info_psicologo.*
import kotlinx.android.synthetic.main.activity_perfil.*

class InfoPsicologo : AppCompatActivity() {

    //private val extras = intent.extras
    private val db = FirebaseFirestore.getInstance()
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_psicologo)

        mostrarLosDatos()

        val extras = intent.extras
        val correo = extras?.getString("correo")

        bt_infoP_añadirReseña.setOnClickListener{
            val intent = Intent(this, AnadirResena::class.java)
            intent.putExtra("correo", correo)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
            startActivity(intent)
        }

        reseñas(correo.toString())
    }

    private fun mostrarLosDatos(){

        val extras = intent.extras

        val mombre = extras?.getString("mombre")
        val direccion = extras?.getString("direccion")
        val precio = extras?.getString("precio")
        val n_telefono = extras?.getInt("n_telefono").toString()
        val especialidades = extras?.getString("especialidades")
        val horario = extras?.getString("horario")
        val consulta_online = extras?.getBoolean("consulta_online")
        val consulta_presencial = extras?.getBoolean("consulta_presencial")
        val consulta_telefonica = extras?.getBoolean("consulta_telefonica")
        val foto = extras?.getString("foto")
        val descripcion = extras?.getString("descripcion")
        //val puntuacion_media = extras?.getString("puntuacion_media")


        tv_infoP_nombre.setText(mombre)
        tv_infoP_direccion.setText(direccion)
        tv_infoP_precio.setText(precio)
        tv_infoP_telefono.setText(n_telefono)
        tv_infoP_especialidades.setText(especialidades)
        tv_infoP_horario.setText(horario)

        var consultas = ""
        if(consulta_online == true){
            consultas += "online"
            if((consulta_presencial == true) && (consulta_telefonica == true)){
                consultas += ", "
            }else{
                if ((consulta_presencial == true) || (consulta_telefonica == true)){
                    consultas += " y "
                }
            }
        }
        if(consulta_presencial == true){
            consultas = consultas + "presencial"
            if(consulta_telefonica == true){
                consultas += " y "
            }
        }
        if(consulta_telefonica == true){
            consultas += "telefónica"
        }

        consultas += "."

        tv_infoP_consultas.setText(consultas)
        Glide.with(this).load(foto).error(R.drawable.ic_no_foto).centerCrop().into(iv_infoP_foto)

        tv_infoP_descripcion.setText(descripcion)


    }

    private fun reseñas(correo: String){
        //db.collection("stories").where("author", "==", user.uid).get()

        var listaResenas = mutableListOf<Resenas>()

        Log.d("TAG", "CORREO: "+ user?.email.toString())
        db.collection("resenas").whereEqualTo("correoPsicologo", correo).get().addOnSuccessListener {documents ->
            for (document in documents) {
                Log.d("TAG", "ESTO ES LO QUE OBTENEMOS: ${document.id} => ${document.data.get("comentario")}")
                listaResenas.add(Resenas(document.data.get("nombre") as String, document.data.get("puntuacion") as Double, document.data.get("fecha") as String, document.data.get("comentario") as String))


            }
            resenasRecycler.layoutManager = GridLayoutManager(this, 1)
            resenasRecycler.adapter = ResenaAdapter(listaResenas)
        }

        //resenasRecycler.layoutManager = GridLayoutManager(this, 2)

       // recycler.layoutManager = GridLayoutManager(this, 2)
        //recycler.adapter = MyAdapter(items)
    }

}