package com.uoc.psico.controlador.perfil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.uoc.psico.R
import com.uoc.psico.controlador.MainActivity
import com.uoc.psico.controlador.Perfil
import com.uoc.psico.controlador.ProviderType

import kotlinx.android.synthetic.main.activity_inicio_sesion.*
import kotlinx.android.synthetic.main.activity_publicitarse.*

class InicioSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_sesion)

        //Si se pulsa el botón de registrarse se accede a esa pantalla
        id_InicioS_registrar.setOnClickListener {
            val Intent = Intent(this, Registrarse::class.java)
            Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
            startActivity(Intent)

        }

        bottomNavigationBar() //Menú inferior

        setup()

        //Ocultar el teclado al tocar el fondo
        cl_fondo_inicioSesion.setOnClickListener {
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }

    }


    private fun bottomNavigationBar(){
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_inicioSesion) as BottomNavigationView
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

    private fun setup(){

        title = "Inicio de sesión"


        //Al pulsar el botón de iniciar sesió se comprueba si ningún campo está vacío
        id_inicioSesion.setOnClickListener{
            if (etCorreo.text.isNotEmpty() && etContraseña.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(etCorreo.text.toString(), etContraseña.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        goToProfile(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else{
                        alertError("Ha ocurrido un error en la autenticación del usuario.")
                    }
                }

            }else{
                alertError("Debe introducir el correo y la contraseña.")
            }
        }

    }

    private fun alertError(error: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(error)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goToProfile(correo: String, provider: ProviderType){
        val intent = Intent(this, Perfil::class.java).apply {
            putExtra("correo", correo)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }


}