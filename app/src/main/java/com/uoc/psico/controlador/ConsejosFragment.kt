package com.uoc.psico.controlador

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import com.uoc.psico.R
import com.uoc.psico.controlador.consejos.ConsejosAdapter
import com.uoc.psico.controlador.consejos.InfoConsejo
import com.uoc.psico.modelo.Consejos
import kotlinx.android.synthetic.main.fragment_consejos.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConsejosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsejosFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        return inflater.inflate(R.layout.fragment_consejos, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)



        var listaConsejos = mutableListOf<Consejos>()

        db.collection("consejos").get().addOnSuccessListener{ result ->
            for (i in result){

                listaConsejos.add(
                    Consejos(i.data.get("titulo") as String, i.data.get("foto") as String,
                        i.data.get("contenido") as String, i.data.get("fuente") as String)
                )
            }


            adapter(listaConsejos)

        }


    }

    private fun adapter(listaConsejos: MutableList<Consejos>) {
        consejosReccycler.apply {
            layoutManager =
                LinearLayoutManager(activity)


            // Al pulsar sobre algun elemento del reccycler
            adapter = ConsejosAdapter(listaConsejos) { position ->
                val intent = Intent(getActivity(), InfoConsejo::class.java)
                intent.putExtra("titulo", listaConsejos[position].titulo)
                intent.putExtra("foto", listaConsejos[position].foto)
                intent.putExtra("contenido", listaConsejos[position].contenido)
                intent.putExtra("fuente", listaConsejos[position].fuente)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
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
         * @return A new instance of fragment ConsejosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsejosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}