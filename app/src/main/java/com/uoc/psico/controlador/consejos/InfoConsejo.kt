package com.uoc.psico.controlador.consejos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.controlador.perfil.InicioSesion
import com.uoc.psico.controlador.MainActivity
import com.uoc.psico.controlador.Perfil
import kotlinx.android.synthetic.main.activity_info_consejo.*

class InfoConsejo : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_consejo)

        auth = Firebase.auth

        bottomNavigationBar() //Menú inferior
        mostrarDatos() //Mostrar la información

    }

    private fun bottomNavigationBar(){
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_infoConsejo) as BottomNavigationView
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


    private fun mostrarDatos() {
        val extras = intent.extras
        val titulo = extras?.getString("titulo")
        val foto = extras?.getString("foto")
        val contenido = extras?.getString("contenido")
        val fuente = extras?.getString("fuente")


        /* para poder detectar y añadir los saltos de línea, se ha añadido :: en donde va un salto de línes
           esto se hace porque firestore suprime los saltos de línea. */
        val delim = "::"

        val list = contenido?.split(delim)
        var newContenido= ""

        if (list != null) {
            for(i in list){
                newContenido += i + "\n \n"
            }
            tv_consejo_contenido.setText(newContenido)
        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////

        Glide.with(this).load(foto).into(iv_consejo_foto)
        tv_consejo_titulo.setText(titulo)

        tv_consejo_fuente.setText(fuente)

    }


    //Menú superior
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)

        //Se oculta el botón de la lupa de buscar
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