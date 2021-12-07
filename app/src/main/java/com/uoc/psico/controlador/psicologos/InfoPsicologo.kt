package com.uoc.psico.controlador.psicologos

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.controlador.MainActivity
import com.uoc.psico.controlador.Perfil
import com.uoc.psico.controlador.perfil.InicioSesion
import com.uoc.psico.modelo.Psicologos
import com.uoc.psico.modelo.Resenas
import kotlinx.android.synthetic.main.activity_info_consejo.*
import kotlinx.android.synthetic.main.activity_info_psicologo.*

class InfoPsicologo : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val user = Firebase.auth.currentUser
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_psicologo)

        auth = Firebase.auth

        bottomNavigationBar() //menú inferior

        val extras = intent.extras
        val correo = extras?.getString("correo")

        mostrarLosDatos(correo)

        botonAnadirResena(correo)

        reseñas(correo.toString())
    }

    private fun botonAnadirResena(correo: String?) {
        bt_infoP_añadirReseña.setOnClickListener{

            //Si el usuario no se ha autenticado lo envía a la pantalla de iniciar sesión

            val currentUser = auth.currentUser

            if(currentUser != null){
                val intent = Intent(this, AnadirResena::class.java)
                intent.putExtra("correo", correo)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                startActivity(intent)
            }else{
                val Intent = Intent(this, InicioSesion::class.java)
                Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                startActivity(Intent)
            }




        }
    }


    private fun bottomNavigationBar(){
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_infoPsicologo) as BottomNavigationView
        //Ningún elemento seleccionado
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true)

        //Listener del menú inferior para saber que botón se ha pulsado
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.psicologosFragment -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("seleccionado", "psicologosFragment")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(intent)
                }
                R.id.foroFragment -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("seleccionado", "foroFragment")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(intent)
                }
                R.id.consejosFragment -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("seleccionado", "consejosFragment")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(intent)
                }

            }
            true
        })

    }

    //Se muestra toda la información del psicólogo
    private fun mostrarLosDatos(correo: String?) {

        if (correo != null) {
            db.collection("psicologos").document(correo).get().addOnSuccessListener{
                val psicologo = Psicologos(correo, it.get("nombre") as String, it.get("provincia") as String, it.get("ciudad") as String,
                        it.get("direccion") as String, it.get("precio") as String, (it.get("n_telefono") as Number).toInt(),
                        it.get("especialidades") as String, it.get("horario") as String, it.get("consulta_online") as Boolean,
                        it.get("consulta_presencial") as Boolean, it.get("consulta_telefonica") as Boolean,
                        it.get("foto") as String, it.get("descripcion") as String, (it.get("puntuacion_media") as Number).toDouble())

                tv_infoP_nombre.setText(psicologo.nombre)


                //Mostramos la ciudad o municipio con la primera letra de cada palabra en mayúscula.
                val list = psicologo.ciudad?.split(" ")
                var newCiudad= ""

                if (list != null) {
                    for(i in list){
                        newCiudad += i.toUpperCase()[0].toString() + i.substring(1, i.length).toLowerCase() + " "
                    }
                }


                if(psicologo.ciudad.toLowerCase()  == psicologo.provincia.toLowerCase()){
                    tv_infoP_direccion.setText(psicologo.direccion + ", " + newCiudad + ".")
                }else {
                    tv_infoP_direccion.setText(psicologo.direccion + ", " + newCiudad + ", " + psicologo.provincia + ".")
                }


                tv_infoP_precio.setText(psicologo.precio + " €/h")
                tv_infoP_telefono.setText(psicologo.n_telefono.toString())
                tv_infoP_especialidades.setText(psicologo.especialidades)
                tv_infoP_horario.setText(psicologo.horario)

                var consultas = ""
                if(psicologo.consulta_online == true){
                    consultas += "online"
                    if((psicologo.consulta_presencial == true) && (psicologo.consulta_telefonica == true)){
                        consultas += ", "
                    }else{
                        if ((psicologo.consulta_presencial == true) || (psicologo.consulta_telefonica == true)){
                            consultas += " y "
                        }
                    }
                }
                if(psicologo.consulta_presencial == true){
                    consultas = consultas + "presencial"
                    if(psicologo.consulta_telefonica == true){
                        consultas += " y "
                    }
                }
                if(psicologo.consulta_telefonica == true){
                    consultas += "telefónica"
                }

                consultas += "."

                tv_infoP_consultas.setText(consultas)
                Glide.with(this).load(psicologo.foto).error(R.drawable.ic_no_foto).centerCrop().into(iv_infoP_foto)

                tv_infoP_descripcion.setText(psicologo.descripcion)


            }
        }


    }

    private fun reseñas(correo: String){

        var sumaPuntuaciones = 0.0
        var resenasSize = 0


        var listaResenas = mutableListOf<Resenas>()

        //Se buscan las reseñas que tenga ese psicólogo en la base de datos.
        db.collection("resenas").whereEqualTo("correoPsicologo", correo).get().addOnSuccessListener { documents ->
            for (document in documents) {
                listaResenas.add(Resenas(document.data.get("nombre") as String, document.data.get("puntuacion") as Double, document.data.get("fecha") as String, document.data.get("comentario") as String))
                resenasSize++
                sumaPuntuaciones += (document.data.get("puntuacion") as Double)
            }
            resenasRecycler.layoutManager = GridLayoutManager(this, 1)
            resenasRecycler.adapter = ResenaAdapter(listaResenas)

            //Se realiza el cálculo de la puntuación media de todas las reseñas y se actualiza en la base de datos
            if(sumaPuntuaciones != 0.0){
                rb_infoPsicologo.setVisibility(RatingBar.VISIBLE)
                tv_infoP_noResenas.setVisibility(TextView.INVISIBLE)
                rb_infoPsicologo.rating = ((sumaPuntuaciones/resenasSize).toFloat())
                db.collection("psicologos").document(correo).update("puntuacion_media", (sumaPuntuaciones / resenasSize) as Number)
            }

        }

    }

    //Menú de la barra superior
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)

        //Se oculta el botón de la lupa
        val item = menu!!.findItem(R.id.busqueda_id)
        item.setVisible(false)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.perfil_id -> {

                val currentUser = auth.currentUser

                if (currentUser != null) {
                    val Intent = Intent(this, Perfil::class.java)
                    Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(Intent)
                } else {
                    val Intent = Intent(this, InicioSesion::class.java)
                    Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(Intent)
                }

            }

        }
        return super.onOptionsItemSelected(item)
    }


}