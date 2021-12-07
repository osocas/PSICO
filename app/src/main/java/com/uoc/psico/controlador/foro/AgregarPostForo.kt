package com.uoc.psico.controlador.foro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.controlador.perfil.InicioSesion
import com.uoc.psico.controlador.MainActivity
import com.uoc.psico.controlador.Perfil
import com.uoc.psico.modelo.Foro
import kotlinx.android.synthetic.main.activity_agregar_post_foro.*
import kotlinx.android.synthetic.main.activity_publicitarse.*
import java.text.SimpleDateFormat
import java.util.*

class AgregarPostForo : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val user = Firebase.auth.currentUser
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_post_foro)

        auth = Firebase.auth

        bottomNavigationBar() //Menú inferior

        botonAnadirPostForo()

        //Ocultar el teclado al tocar el fondo
        cl_fondo_anadirPost.setOnClickListener {
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }

    }

    private fun bottomNavigationBar(){

        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_agregar_post_foro) as BottomNavigationView
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


    fun botonAnadirPostForo(){
        bt_agregarForo.setOnClickListener{
            if (user != null) {
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())

                db.collection("usuarios").document(user.email.toString()).get().addOnSuccessListener {
                    //Se guarda la información en la base de datos
                    Foro(user.email.toString(), it.get("nombre") as String? + " " + it.get("apellidos") as String?, et_agregarForo.text.toString()).addPostDB(Date())

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("seleccionado", "foroFragment")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(intent)

                }
            }
        }
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