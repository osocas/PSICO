package com.uoc.psico.controlador.perfil

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uoc.psico.R
import com.uoc.psico.controlador.MainActivity
import com.uoc.psico.controlador.Perfil
import com.uoc.psico.modelo.Psicologos
import kotlinx.android.synthetic.main.activity_publicitarse.*


class Publicitarse : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private val user = Firebase.auth.currentUser
    private val File = 1

    private var foto = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publicitarse)

        auth = Firebase.auth

        bottomNavigationBar()

        iv_publi_foto.setOnClickListener{
            fileUpload()
        }


        setup()

    }

    private fun bottomNavigationBar(){

        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_publicitarse) as BottomNavigationView
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

    }

    private fun setup(){
        bt_publi_publicar.setOnClickListener{
            if (et_publi_nombre.text.isNotEmpty() && et_publi_dirección.text.isNotEmpty() &&
                    et_publi_precio.text.isNotEmpty() && et_publi_telefono.text.isNotEmpty() &&
                    et_publi_especialidades.text.isNotEmpty() && et_publi_horario.text.isNotEmpty() &&
                    et_publi_descripción.text.isNotEmpty() && (cb_publi_online.isChecked || cb_publi_presencial.isChecked || cb_publi_telefonica.isChecked)){



               // Log.d("TAG", "cb_publi_online.isChecked: " + cb_publi_online.isChecked + " otro " + cb_publi_presencial.isChecked + " otro " + cb_publi_telefonica.isChecked)

                if (user != null) {


                    //Guardamos el psicólogo publicitado en la BD
                    Psicologos(
                        user.email.toString(),
                        et_publi_nombre.text.toString(),
                        et_publi_provincia.text.toString(),
                        et_publi_ciudad.text.toString(),
                        et_publi_dirección.text.toString(),
                        et_publi_precio.text.toString(),
                        et_publi_telefono.text.toString().toInt(),
                        et_publi_especialidades.text.toString(),
                        et_publi_horario.text.toString(),
                        cb_publi_online.isChecked,
                        cb_publi_presencial.isChecked,
                        cb_publi_telefonica.isChecked,
                        foto,
                        et_publi_descripción.text.toString(),
                        99.0
                    ).addPsicologo()


                    /*db.collection("psicologos").document(user.email.toString()).set(
                            hashMapOf("correo" to user.email.toString(),
                                    "nombre" to et_publi_nombre.text.toString(),
                                    "direccion" to et_publi_dirección.text.toString(),
                                    "precio" to et_publi_precio.text.toString(),
                                    "n_telefono" to et_publi_telefono.text.toString().toInt(),
                                    "especialidades" to et_publi_especialidades.text.toString(),
                                    "horario" to et_publi_horario.text.toString(),
                                    "consulta_online" to cb_publi_online.isChecked,
                                    "consulta_presencial" to cb_publi_presencial.isChecked,
                                    "consulta_telefonica" to cb_publi_telefonica.isChecked,
                                    "foto" to foto,
                                    "descripcion" to et_publi_descripción.text.toString(),
                                    "puntuacion_media" to 5)
                    )*/


                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                    startActivity(intent)
                }


            }else{
                alertError("Para poder publicitarse debe rellenar todos los campos del formulario")
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

    //Abrimos la galería del móvil
    fun fileUpload() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type= "*/*"
        startActivityForResult(intent, File)
    }

    //Una vez seleccionada la imagen se guarda en el Storage de Firebase en la carpeta User
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == File) {
            if (resultCode == RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference =
                        FirebaseStorage.getInstance().getReference().child("Psicologos")
                val file_name: StorageReference = Folder.child("file" + FileUri!!.lastPathSegment)
                file_name.putFile(FileUri).addOnSuccessListener { taskSnapshot ->
                    file_name.getDownloadUrl().addOnSuccessListener { uri ->

                        foto = java.lang.String.valueOf(uri).toString()

                        //Modificamos el campo foto  de la BD para añadir la foto obtenída
                        //if (user != null) {
                           // db.collection("usuarios").document(user.email.toString()).update("foto", url)
                        //}

                        //Mostramos la imagen
                        Glide.with(this).load(foto).error(R.drawable.ic_foto_perfil).centerCrop().into(
                            iv_publi_foto
                        )

                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)

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