package com.uoc.psico.controlador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.modelo.Usuario
import kotlinx.android.synthetic.main.activity_registrarse.*




class Registrarse : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)


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
                    //MainActivity.openFragment(PsicologosFragment.newInstance("", ""))
                    // openFragment(PsicologosFragment.newInstance("", ""))
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

        setup()
    }


    private fun setup(){

        title = "Autenticación"

        id_registrarme.setOnClickListener{

            if (rb_si.isChecked){
                Log.d("TAG", "chaequeado el si")
            }

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

                            val usuario = Usuario(et_Registro_Correo.text.toString(),
                                et_registro_nombre.text.toString(), et_registro_apellido.text.toString(),
                                et_registro_edad.text.toString().toInt(), et_registro_ciudad.text.toString(), psicologo)


                            saveDataDB(usuario)
                            goToProfile(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else{
                            alertError("Ha ocurrido un error en la autenticación del usuario")
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

    private fun saveDataDB(usuario: Usuario){
        db.collection("usuarios").document(usuario.correo).set(
            hashMapOf("nombre" to usuario.nombre,
            "apellidos" to usuario.apellidos,
            "edad" to usuario.edad,
            "ciudad" to usuario.ciudad,
            "psicologo" to usuario.psicologo)
        )
    }



}