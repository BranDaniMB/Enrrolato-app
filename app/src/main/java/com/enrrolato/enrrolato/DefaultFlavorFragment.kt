package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.enrrolato.enrrolato.createIcecream.FlavorsFragment

class DefaultFlavorFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_default_flavor, container, false)

        val backPrincipal = view.findViewById<View>(R.id.btBackToPrincipal) as ImageButton
        val makeIcecream = view.findViewById<View>(R.id.btCreateOurIcecream) as Button

        backPrincipal.setOnClickListener {
            backToPrincipal()
        }

        makeIcecream.setOnClickListener {
            makeOwns()
        }

        return view
    }

    private fun backToPrincipal() {
        val fragment = PrincipalMenuFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_default, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun makeOwns() {
        val fragment = FlavorsFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_default, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}