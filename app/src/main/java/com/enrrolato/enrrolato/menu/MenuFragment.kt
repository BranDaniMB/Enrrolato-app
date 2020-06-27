package com.enrrolato.enrrolato.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R

class MenuFragment : Fragment() {

    private var choose:Boolean = true // SI ESTÁ ACTIVADO VA A "JARABES", SI ESTÁ DESACTIVADO, VA A "TOPPINGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_choose_menu, container, false)
        var back = view.findViewById<View>(R.id.btBackToPrincipal2) as ImageButton
        var flavorM = view.findViewById<View>(R.id.btShowFlavors) as Button
        var fillingM = view.findViewById<View>(R.id.btShowFilling) as Button
        var toppingM = view.findViewById<View>(R.id.btShowTopping) as Button

        back.setOnClickListener {
            backToPrincipal()
        }

        flavorM.setOnClickListener {
            chargeMFlavors()
        }

        fillingM.setOnClickListener {
            choose = true
            chargeMFillingTopping(choose)
        }

        toppingM.setOnClickListener {
            choose = false
            chargeMFillingTopping(choose)
        }

        return view
    }

    private fun backToPrincipal() {
        val fragment = PrincipalMenuFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_choose_menu, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun chargeMFlavors() {
        val fragment = ChooseFlavorFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_choose_menu, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun chargeMFillingTopping(flag: Boolean) {
        val fragment = ChooseMenuFTFragment()
        fragment.selector(flag)
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_choose_menu, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}