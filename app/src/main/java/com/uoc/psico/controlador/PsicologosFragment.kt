package com.uoc.psico.controlador

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.modelo.Psicologos
import kotlinx.android.synthetic.main.activity_perfil.*
import kotlinx.android.synthetic.main.fragment_psicologos.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PsicologosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PsicologosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //private val user = Firebase.auth.currentUser
    private val db = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_psicologos, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)



        var listaPsicologos = mutableListOf<Psicologos>()

        db.collection("psicologos").get().addOnSuccessListener{ result ->
            for (i in result){
                //Log.d("TAG", "${i.id} => ${i.data}") //${i.data.get("apellidos") as String}
                val psicologo = Psicologos(i.data.get("correo") as String, i.data.get("mombre") as String,
                        i.data.get("direccion") as String, i.data.get("precio") as String, (i.data.get("n_telefono") as Number).toInt(),
                        i.data.get("especialidades") as String, i.data.get("horario") as String, i.data.get("consulta_online") as Boolean,
                        i.data.get("consulta_presencial") as Boolean, i.data.get("consulta_telefonica") as Boolean,
                        i.data.get("foto") as String, i.data.get("descripcion") as String, (i.data.get("puntuacion_media") as Number).toDouble())

                listaPsicologos.add(psicologo)
            }

            Log.d("TAG", "CONTIENE: " + listaPsicologos[0].correo)
            adapter(listaPsicologos)



        }








            //tv_perfil_nombre.setText(it.get("nombre") as String? + " " + it.get("apellidos") as String?)



    }

    private fun adapter(listaPsicologos: MutableList<Psicologos>){
        psicologosReccycler.apply {
            layoutManager =
                    LinearLayoutManager(activity)
            //adapter = PsicologosAdapter()

            // Al pulsar sobre algun elemento del reccycler
            adapter = PsicologosAdapter(listaPsicologos) { position ->
                // do something
                Toast.makeText(activity, "yoou clecked on item no: $position", Toast.LENGTH_SHORT).show()

                val intent = Intent(activity, InfoPsicologo::class.java)
                //intent.putExtra("psicologoSelect", listaPsicologos[position])


                intent.putExtra("correo", listaPsicologos[position].correo)
                intent.putExtra("mombre", listaPsicologos[position].mombre)
                intent.putExtra("direccion", listaPsicologos[position].direccion)
                intent.putExtra("precio", listaPsicologos[position].precio)
                intent.putExtra("n_telefono", listaPsicologos[position].n_telefono)
                intent.putExtra("especialidades", listaPsicologos[position].especialidades)
                intent.putExtra("horario", listaPsicologos[position].horario)
                intent.putExtra("consulta_online", listaPsicologos[position].consulta_online)
                intent.putExtra("consulta_presencial", listaPsicologos[position].consulta_presencial)
                intent.putExtra("consulta_telefonica", listaPsicologos[position].consulta_telefonica)
                intent.putExtra("foto", listaPsicologos[position].foto)
                intent.putExtra("descripcion", listaPsicologos[position].descripcion)
                intent.putExtra("puntuacion_media", listaPsicologos[position].puntuacion_media)

                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                startActivity(intent)

            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PsicologosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PsicologosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}