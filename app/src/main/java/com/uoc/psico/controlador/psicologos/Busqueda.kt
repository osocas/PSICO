package com.uoc.psico.controlador.psicologos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.controlador.perfil.InicioSesion
import com.uoc.psico.controlador.MainActivity
import com.uoc.psico.controlador.Perfil
import kotlinx.android.synthetic.main.activity_busqueda.*
import kotlinx.android.synthetic.main.activity_publicitarse.*
import kotlinx.android.synthetic.main.activity_registrarse.*

class Busqueda : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private var rangoPrecio = 9999
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)

        auth = Firebase.auth

        title = "Búsqueda de psicólogo/a"

        bottomNavigationBar()

        spinner()

        buscar()

        //Ocultar el teclado al tocar el fondo
        cl_fondo_busqueda.setOnClickListener {
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }

    }


    private fun bottomNavigationBar(){
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_busqueda) as BottomNavigationView


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

    //lista espiner para seleccionar el rengo de precios
    private fun spinner() {

        val listaPrecios = listOf("Ver todos", "< 20€", "< 40€", "< 60€", "< 80€")

        val adaptador = ArrayAdapter(this, R.layout.item_spinner_busqueda, listaPrecios)

        spinnerPrecios.adapter = adaptador

        spinnerPrecios.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                /*Si el que se ha seleccionado es el de la posición 0 (Ver todos), el rango de precio se deja por defecto a 9999
                para que en la búsqueda muestre todos aquellos que estén por debajo de ese precio, es decir, todos. */

                when (p2){
                    1 -> rangoPrecio = 20
                    2 -> rangoPrecio = 40
                    3 -> rangoPrecio = 60
                    4 -> rangoPrecio = 80
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }


    private fun buscar() {

        //Si se pulsa el botón de buscar comprueba que los dos primeros campos no estén vacíos, los otros si pueden estar vacíos.
        bt_buscar.setOnClickListener {

            if (et_busqueda_ciudad.text.isNotEmpty() && et_busqueda_provincia.text.isNotEmpty()){
                var listaBusqueda = mutableListOf<String>()

                //Busca en la base de datos por la ciudad o municipio escrito
                db.collection("psicologos").whereEqualTo("ciudad", et_busqueda_ciudad.text.toString().toLowerCase()).get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        if((document.data.get("provincia") as String).equals(
                                        et_busqueda_provincia.text.toString(),
                                        true
                                ) && ((document.data.get("precio") as String).toInt() <= rangoPrecio) ){
                            if(cb_busqueda_online.isChecked && (document.data.get("consulta_online") as Boolean) == true ){
                                listaBusqueda.add(document.data.get("correo") as String)
                            }else{
                                if(cb_busqueda_presenciales.isChecked && (document.data.get("consulta_presencial") as Boolean) == true ){
                                    listaBusqueda.add(document.data.get("correo") as String)
                                }else{
                                    if(cb_busqueda_telefonica.isChecked && (document.data.get("consulta_telefonica") as Boolean) == true ){
                                        listaBusqueda.add(document.data.get("correo") as String)
                                    }
                                }
                            }

                            //Si no se ha marcado ningún tipo de consulta se muestran de todos los tipos
                            if(cb_busqueda_online.isChecked == false && cb_busqueda_presenciales.isChecked == false && cb_busqueda_telefonica.isChecked == false) {
                                listaBusqueda.add(document.data.get("correo") as String)
                            }

                        }

                    }

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("seleccionado", "psicologosFragment")
                    intent.putExtra("listaBusqueda", listaBusqueda as ArrayList<String>)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(intent)
                }

            }else{
                alertError("Tanto el campo de municipio o ciudad, como el de provincia deben rellenarse para realizar la búsqueda.")
            }
        }

    }

    private fun alertError(text: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(text)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    //menú barra superior
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