package com.uoc.psico.controlador.psicologos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_anadir_resena.*
import kotlinx.android.synthetic.main.activity_publicitarse.*
import java.text.SimpleDateFormat
import java.util.*

class AnadirResena : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_resena)

        auth = Firebase.auth

        bottomNavigationBar() //menú inferior

        val extras = intent.extras
        val correo = extras?.getString("correo")


        botonComptartirReseña(correo.toString())

        //Ocultar el teclado al tocar el fondo
        cl_fondo_anadirResena.setOnClickListener {
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }


    }

    private fun bottomNavigationBar(){
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_agregar_resena) as BottomNavigationView
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

    private fun botonComptartirReseña(correo: String){
        bt_compartir_resena.setOnClickListener{


            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())

            if (user != null) {

                var nombre = ""
                db.collection("usuarios").document(user.email.toString()).get().addOnSuccessListener {


                    //Guardamos la reseña en la base de datos
                    db.collection("resenas").add(
                        hashMapOf("correoPsicologo" to correo,
                            "nombre" to (it.get("nombre") as String? + " " + it.get("apellidos") as String?),
                            "puntuacion" to rb_anadir_resena.rating.toDouble(),
                            "fecha" to currentDate,
                            "comentario" to et_anadir_resena.text.toString())
                    ).addOnSuccessListener {
                        val intent = Intent(this, InfoPsicologo::class.java)
                        intent.putExtra("correo", correo)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                        startActivity(intent)
                    }


                }

            }

        }
    }


    //Menú superior
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)

        //ocultamos el botón de la lupa
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