package com.uoc.psico.controlador

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.uoc.psico.R
import com.uoc.psico.modelo.Foro
import com.uoc.psico.modelo.Psicologos
import kotlinx.android.synthetic.main.fragment_foro.*
import kotlinx.android.synthetic.main.fragment_psicologos.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private val user = Firebase.auth.currentUser
    //private var layoutManager: RecyclerView.LayoutManager? = null
   // private var adapter: RecyclerView.Adapter<ForoAdapter.ViewHolder>? = null

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
        return inflater.inflate(R.layout.fragment_foro, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        auth = Firebase.auth

        /*foroReccycler.apply {
            layoutManager =
                LinearLayoutManager(activity)
            adapter = ForoAdapter()
        }*/


        var listaPosts = mutableListOf<Foro>()

        db.collection("foro").get().addOnSuccessListener{ result ->
            for (i in result){

                listaPosts.add(
                    Foro(i.data.get("correo") as String, i.data.get("nombre") as String,
                    i.data.get("post") as String)
                )
            }

            adapter(listaPosts)

        }


        if (user != null) {
            //tv_perfil_correo.setText(user.email.toString())
            db.collection("usuarios").document(user.email.toString()).get().addOnSuccessListener{
                if((it.get("psicologo") as Boolean) == true){
                    bt_añadir.setVisibility(Button.VISIBLE)

                    bt_añadir.setOnClickListener{
                        val intent = Intent(getActivity(), AgregarPostForo::class.java)
                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION) //quitar la animación entre activitys
                        startActivity(intent)
                    }
                }
            }
        }

        //bt_añadir.setVisibility(Button.VISIBLE)
    }


    private fun adapter(listaPosts: MutableList<Foro>) {
        foroReccycler.apply {
            layoutManager =
                LinearLayoutManager(activity)


            // Al pulsar sobre algun elemento del reccycler
            adapter = ForoAdapter(listaPosts) { position ->
                val intent = Intent(getActivity(), InfoPsicologo::class.java)
                intent.putExtra("correo", listaPosts[position].correo)
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
         * @return A new instance of fragment ForoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}