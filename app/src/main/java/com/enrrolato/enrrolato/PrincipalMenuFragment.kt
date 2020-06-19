package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.enrrolato.enrrolato.createIcecream.FlavorsFragment

/**
 * A simple [Fragment] subclass.
 */
class PrincipalMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_principal_menu, container, false)
        val create = view.findViewById<View>(R.id.btCreateIceCream) as Button

        create.setOnClickListener {
            createIceCream()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun createIceCream() {
        val fragment = DefaultFlavorFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction. replace(R.id.ly_principal, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun viewMenu() {
        // CODE HERE
    }

    fun seasonIceCream() {
        // CODE HERE
    }

}
