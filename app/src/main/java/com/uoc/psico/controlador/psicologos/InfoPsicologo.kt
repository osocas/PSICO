package com.uoc.psico.controlador.psicologos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RatingBar
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.controlador.perfil.InicioSesion
import com.uoc.psico.controlador.MainActivity
import com.uoc.psico.controlador.Perfil
import com.uoc.psico.modelo.Psicologos
import com.uoc.psico.modelo.Resenas
import kotlinx.android.synthetic.main.activity_info_psicologo.*

class InfoPsicologo : AppCompatActivity() {

    //private val extras = intent.extras
    private val db = FirebaseFirestore.getInstance()
    private val user = Firebase.auth.currentUser
    private lateinit var auth: FirebaseAuth

    //private lateinit var psicologocc: Psicologos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_psicologo)

        auth = Firebase.auth

        bottomNavigationBar()




        val extras = intent.extras
        val correo = extras?.getString("correo")

        mostrarLosDatos(correo)

        botonAnadirResena(correo)



        reseñas(correo.toString())
    }

    private fun botonAnadirResena(correo: String?) {
        bt_infoP_añadirReseña.setOnClickListener{


            val currentUser = auth.currentUser

            if(currentUser != null){
                val intent = Intent(this, AnadirResena::class.java)
                intent.putExtra("correo", correo)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                startActivity(intent)
            }else{
                val Intent = Intent(this, InicioSesion::class.java)
                Intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                startActivity(Intent)
            }




        }
    }


    private fun bottomNavigationBar(){
        val bottomNavigationView = findViewById<View>(R.id.bottomNavigationView_infoPsicologo) as BottomNavigationView
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

    private fun mostrarLosDatos(correo: String?) {

        /*val extras = intent.extras

        val nombre = extras?.getString("nombre")
        val direccion = extras?.getString("direccion")
        val precio = extras?.getString("precio")
        val n_telefono = extras?.getInt("n_telefono").toString()
        val especialidades = extras?.getString("especialidades")
        val horario = extras?.getString("horario")
        val consulta_online = extras?.getBoolean("consulta_online")
        val consulta_presencial = extras?.getBoolean("consulta_presencial")
        val consulta_telefonica = extras?.getBoolean("consulta_telefonica")
        val foto = extras?.getString("foto")
        val descripcion = extras?.getString("descripcion")*/
        //val puntuacion_media = extras?.getString("puntuacion_media")


        if (correo != null) {
            db.collection("psicologos").document(correo).get().addOnSuccessListener{
                val psicologo = Psicologos(correo, it.get("nombre") as String, it.get("provincia") as String, it.get("ciudad") as String,
                    it.get("direccion") as String, it.get("precio") as String, (it.get("n_telefono") as Number).toInt(),
                    it.get("especialidades") as String, it.get("horario") as String, it.get("consulta_online") as Boolean,
                    it.get("consulta_presencial") as Boolean, it.get("consulta_telefonica") as Boolean,
                    it.get("foto") as String, it.get("descripcion") as String, (it.get("puntuacion_media") as Number).toDouble())

                //Log.d("TAG", "Esto es lo que hay en Psicologo: " + picologo.)
                tv_infoP_nombre.setText(psicologo.nombre)

                tv_infoP_direccion.setText(psicologo.direccion + ", " + psicologo.ciudad + ", " + psicologo.provincia)
                tv_infoP_precio.setText(psicologo.precio + " €/h")
                tv_infoP_telefono.setText(psicologo.n_telefono.toString())
                tv_infoP_especialidades.setText(psicologo.especialidades)
                tv_infoP_horario.setText(psicologo.horario)

                var consultas = ""
                if(psicologo.consulta_online == true){
                    consultas += "online"
                    if((psicologo.consulta_presencial == true) && (psicologo.consulta_telefonica == true)){
                        consultas += ", "
                    }else{
                        if ((psicologo.consulta_presencial == true) || (psicologo.consulta_telefonica == true)){
                            consultas += " y "
                        }
                    }
                }
                if(psicologo.consulta_presencial == true){
                    consultas = consultas + "presencial"
                    if(psicologo.consulta_telefonica == true){
                        consultas += " y "
                    }
                }
                if(psicologo.consulta_telefonica == true){
                    consultas += "telefónica"
                }

                consultas += "."

                tv_infoP_consultas.setText(consultas)
                Glide.with(this).load(psicologo.foto).error(R.drawable.ic_no_foto).centerCrop().into(iv_infoP_foto)

                tv_infoP_descripcion.setText(psicologo.descripcion)







            }
        }


    }

    private fun reseñas(correo: String){
        //db.collection("stories").where("author", "==", user.uid).get()


        //BDBackground().execute(correo)

        //resenasRecycler.layoutManager = GridLayoutManager(this, 1)
        //resenasRecycler.adapter = ResenaAdapter(BDBackground().execute(correo))
        //bDBackground.execute(correo)
        var sumaPuntuaciones = 0.0
        var resenasSize = 0


        var listaResenas = mutableListOf<Resenas>()

        Log.d("TAG", "CORREO: "+ user?.email.toString())
        db.collection("resenas").whereEqualTo("correoPsicologo", correo).get().addOnSuccessListener {documents ->
            for (document in documents) {
                Log.d("TAG", "ESTO ES LO QUE OBTENEMOS: ${document.id} => ${document.data.get("comentario")}")
                listaResenas.add(Resenas(document.data.get("nombre") as String, document.data.get("puntuacion") as Double, document.data.get("fecha") as String, document.data.get("comentario") as String))
                resenasSize++
                sumaPuntuaciones += (document.data.get("puntuacion") as Double)
            }
            resenasRecycler.layoutManager = GridLayoutManager(this, 1)
            resenasRecycler.adapter = ResenaAdapter(listaResenas)

            //Se muestran las estrellas con la puntuación media de las reseñas
            rb_infoPsicologo.setVisibility(RatingBar.VISIBLE)
            rb_infoPsicologo.rating = ((sumaPuntuaciones/resenasSize).toFloat())

            //if (user != null) {
                db.collection("psicologos").document(correo).update("puntuacion_media", (sumaPuntuaciones/resenasSize) as Number)
            //}
        }

        //resenasRecycler.layoutManager = GridLayoutManager(this, 2)

       // recycler.layoutManager = GridLayoutManager(this, 2)
        //recycler.adapter = MyAdapter(items)
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