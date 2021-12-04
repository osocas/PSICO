package com.uoc.psico.controlador

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.uoc.psico.R
import com.uoc.psico.controlador.perfil.Publicitarse
import kotlinx.android.synthetic.main.activity_perfil.*


enum class ProviderType{
    BASIC
}



class Perfil : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    private val user = Firebase.auth.currentUser
    private val File = 1
   // private val database = FirebaseFirestore.database
   // val myRef = database.getReference("user")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        auth = Firebase.auth


        bottomNavigationBar()


        //val bundle = intent.extras
        //val correo = bundle?.getString("correo")
        //val provider = bundle?.getString("provider")


        iv_imagen_perfil.setOnClickListener{
            fileUpload()
        }

        setup()//correo ?: "", provider ?: "")

    }

    private fun bottomNavigationBar(){
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_perfil) as BottomNavigationView


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



    private fun setup(){//correo: String, provider: String){
        title = "Perfil"
        //tv_usuarioID.text = correo
        //tv_providerID.text = provider




        if (user != null) {
            //tv_perfil_correo.setText(user.email.toString())
            db.collection("usuarios").document(user.email.toString()).get().addOnSuccessListener{
                tv_perfil_nombre.setText(it.get("nombre") as String? + " " + it.get("apellidos") as String?)
                tv_perfil_edad.setText((it.get("edad") as Number?).toString() + " años")
                tv_perfil_ciudad.setText(it.get("ciudad") as String?)
                tv_perfil_correo.setText(user.email.toString())

                Glide.with(this).load(it.get("foto") as String?).error(R.drawable.ic_foto_perfil).centerCrop().into(iv_imagen_perfil)

                if((it.get("psicologo") as Boolean) == false){

                    //Eliminar el textView de psicólogo/a y el botón de publicitarse
                    (tv_perfil_psicologo.getParent() as ViewGroup).removeView(tv_perfil_psicologo)
                    (bt_perfil_publicitarse.getParent() as ViewGroup).removeView(bt_perfil_publicitarse)

                    //modifico las constraint para que se coloque arriba y depenta del textView del correo
                    val constraintLayout = findViewById<ConstraintLayout>(R.id.id_ConstraintLayout)
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.connect(R.id.bt_cerrar_sesion, ConstraintSet.TOP, R.id.tv_perfil_correo, ConstraintSet.TOP, 160)

                    constraintSet.applyTo(constraintLayout)
                }else{
                    intentPublicitarse()
                }

                //tv_perfil_psicologo.setText("Psicólogo/a")
                //tv_perfil_psicologo.setVisibility(TextView.VISIBLE)
            }
        }

        /*db.collection("usuarios").document(user.email).get().addOnSuccessListener {
            tv_perfil_nombre.setText(it.get("nombre") as String? + it.get("apellidos") as String?)
            tv_perfil_edad.setText(it.get("edad") as String?)
            tv_perfil_ciudad.setText(it.get("ciudad") as String?)
            tv_perfil_correo.setText(correoUsuarioActual)

        }*/

       // Log.d("TAG", "Psicologo esta en: " + psicologo)



        bt_cerrar_sesion.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val Intent = Intent(this, MainActivity::class.java)
            Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
            startActivity(Intent)
        }


    }
    private fun intentPublicitarse(){

        bt_perfil_publicitarse.setOnClickListener{
            val Intent = Intent(this, Publicitarse::class.java)
            Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
            startActivity(Intent)
        }

    }

    //Abrimos la galería del móvil
    fun fileUpload() {
        val intent = Intent (Intent.ACTION_GET_CONTENT)
        intent.type= "*/*"
        startActivityForResult(intent,File)
    }

    //Una vez seleccionada la imagen se guarda en el Storage de Firebase en la carpeta User
    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == File) {
            if (resultCode == RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("User")
                val file_name: StorageReference = Folder.child("file" + FileUri!!.lastPathSegment)
                file_name.putFile(FileUri).addOnSuccessListener { taskSnapshot ->
                    file_name.getDownloadUrl().addOnSuccessListener { uri ->

                        val url = java.lang.String.valueOf(uri).toString()

                        //Modificamos el campo foto  de la BD para añadir la foto obtenída
                        if (user != null) {
                            db.collection("usuarios").document(user.email.toString()).update("foto", url)
                        }

                        //Mostramos la imagen
                        Glide.with(this).load(url).error(R.drawable.ic_foto_perfil).centerCrop().into(iv_imagen_perfil)

                    }
                }
            }
        }
    }

}