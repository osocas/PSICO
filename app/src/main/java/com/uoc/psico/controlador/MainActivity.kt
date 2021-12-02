package com.uoc.psico.controlador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.controlador.perfil.InicioSesion
import com.uoc.psico.controlador.psicologos.Busqueda


class MainActivity : AppCompatActivity() {

    private var perfil = false
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
       /* val recyclerView = findViewById<RecyclerView>(R.id.psicologosReccycler)
        val adapter = PsicologosAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter*/

       /* val user = Firebase.auth.currentUser
        if (user != null) {
            correoUsuarioActual = user.email
        }
        Log.d("TAG", "El currentUser es: " + (user?.email ?: "no hay"))*/

        //Control del menú de abajo
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        //Nombre en la barra superior
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.psicologosFragment,
            R.id.foroFragment,
            R.id.consejosFragment
        ))

        setupActionBarWithNavController(navController, appBarConfiguration)


        bottomNavigationView.setupWithNavController(navController)


        val extras = intent.extras
        val seleccionado = extras?.getString("seleccionado")

        when (seleccionado) {
            "consejosFragment" -> bottomNavigationView.setSelectedItemId(R.id.consejosFragment)
            "foroFragment" -> bottomNavigationView.setSelectedItemId(R.id.foroFragment)
            else -> { // Note the block
                bottomNavigationView.setSelectedItemId(R.id.psicologosFragment)
            }
        }


        //val casa = R.id.foroFragment
        //botón seleccionado
       // bottomNavigationView.setSelectedItemId(R.id.foroFragment)
        //-----------------





    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.perfil_id -> {

                val currentUser = auth.currentUser

                if(currentUser != null){
                    val Intent = Intent(this, Perfil::class.java)
                    Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(Intent)
                }else{
                    val Intent = Intent(this, InicioSesion::class.java)
                    Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(Intent)
                }

            }
            R.id.busqueda_id -> {

                Log.d("TAG", "He entrado dentor de los de la busqueda")
                val Intent = Intent(this, Busqueda::class.java)
                Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                startActivity(Intent)
            }
        }



        return super.onOptionsItemSelected(item)
    }
}