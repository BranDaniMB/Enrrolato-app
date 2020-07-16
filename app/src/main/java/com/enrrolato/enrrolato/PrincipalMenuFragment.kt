package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.enrrolato.enrrolato.createIcecream.DefaultFlavorFragment
import com.enrrolato.enrrolato.menu.MenuFragment

/**
 * A simple [Fragment] subclass.
 */
class PrincipalMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_principal_menu, container, false)
        val create = view.findViewById<View>(R.id.btCreateIceCream) as Button
        val show = view.findViewById<View>(R.id.btShowMenu) as Button
        val season = view.findViewById<View>(R.id.btShowSeasonIcecream) as Button

        create.setOnClickListener {
            createIceCream()
        }

        show.setOnClickListener {
            viewMenu()
        }

        season.setOnClickListener {
            seasonIceCream()
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
        val fragment = MenuFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction. replace(R.id.ly_principal, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun seasonIceCream() {
        // CODE HERE
    }

}
