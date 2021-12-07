package com.uoc.psico.controlador.perfil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager

import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uoc.psico.R
import com.uoc.psico.controlador.MainActivity
import com.uoc.psico.controlador.Perfil
import com.uoc.psico.controlador.ProviderType
import com.uoc.psico.modelo.Usuario
import kotlinx.android.synthetic.main.activity_publicitarse.*
import kotlinx.android.synthetic.main.activity_registrarse.*




class Registrarse : AppCompatActivity() {

    //private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        bottomNavigationBar() //menú inferior

        setup()

        //Ocultar el teclado al tocar el fondo
        cl_fondo_registrarse.setOnClickListener {
            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }
    }


    private fun bottomNavigationBar(){
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_registrarse) as BottomNavigationView
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

        title = "Registrarse"

        id_registrarme.setOnClickListener{

            //Verificamos que todos los campos están rellenos
            if (et_Registro_Correo.text.isNotEmpty() && et_Registro_Contraseña.text.isNotEmpty() &&
                et_registro_nombre.text.isNotEmpty() && et_registro_apellido.text.isNotEmpty() &&
                et_registro_edad.text.isNotEmpty() && et_registro_ciudad.text.isNotEmpty() &&
                et_registro_repContraseña.text.isNotEmpty() && (rb_si.isChecked || rb_no.isChecked)){


                //Comprobamos que las contraseñas son iguales
                if (et_Registro_Contraseña.text.toString() == et_registro_repContraseña.text.toString()){

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(et_Registro_Correo.text.toString(), et_Registro_Contraseña.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){

                            var psicologo = false

                            if (rb_si.isChecked){
                                psicologo = true
                            }

                            //Se guarda el usuarios en la base de datos
                            Usuario(et_Registro_Correo.text.toString(),
                                et_registro_nombre.text.toString(), et_registro_apellido.text.toString(),
                                et_registro_edad.text.toString().toInt(), et_registro_ciudad.text.toString(), psicologo).addUsuario()

                            goToProfile(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else{
                            alertError("Ha ocurrido un error en la autenticación del usuario, vuelva a intentarlo.")
                        }
                    }
                }else{ //si las contraseñas no son ifuales
                    alertError("Las contraseñas no coinciden, vuelva a escribirlas")
                    et_Registro_Contraseña.setText("")
                    et_registro_repContraseña.setText("")
                }


            }else{
                alertError("Para poder registrarse debe rellenar todos los campos del formulario")
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

    private fun goToProfile(correo: String, provider: ProviderType){
        val intent = Intent(this, Perfil::class.java).apply {
            putExtra("correo", correo)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }

}