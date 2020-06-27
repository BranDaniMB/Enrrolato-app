package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.database.Enrrolato

class SelectContainerFragment : Fragment() {

    private lateinit var wrapping: String
    private lateinit var username:String
    private var enrrolato = Enrrolato.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view: View =  inflater.inflate(R.layout.fragment_select_container, container, false)
        var back= view.findViewById<View>(R.id.btBackToTopping) as ImageButton
        var cup = view.findViewById<View>(R.id.btCup) as Button
        var cone = view.findViewById<View>(R.id.btCone) as Button
        var addCart = view.findViewById<View>(R.id.btAddToShopCart) as Button
        var addNewIceCream = view.findViewById<View>(R.id.btAddNewIceCream) as Button

        back.setOnClickListener {
            backToTopping()
        }

        cup.setOnClickListener {
            wrapping = cup.text.toString()
        }

        cone.setOnClickListener {
            wrapping = cone.text.toString()
        }

        addCart.setOnClickListener {

        }

        return view
    }

   private fun backToTopping() {
       val fragment = ToppingFragment() // NO VA A PRINCIPAL SINO A FILLING
       val fm = requireActivity().supportFragmentManager
       val transaction = fm.beginTransaction()
       transaction.replace(R.id.ly_container, fragment)
       transaction.addToBackStack(null)
       transaction.commit()
   }

    private fun addToCart() {
        // ANTES DE AGREGARLA AL CARRITO DEBE MOSTRARSE UN MENSAJE EN DONDE SE LE SOLICITA EL
        // NOMBRE DE USUARIO (SI NO LO TIENE)
    }

    private fun enterUserName() {
    }

}