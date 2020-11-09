package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.enrrolato.enrrolato.createIceCream.DefaultFlavorFragment
import com.enrrolato.enrrolato.menu.MenuFragment
import com.enrrolato.enrrolato.menu.MenuSeasonFragment

class PrincipalMenuFragment : Fragment() {

    private lateinit var create: Button
    private lateinit var show: Button
    private lateinit var season: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_principal_menu, container, false)
        create = view.findViewById(R.id.btCreateIceCream)
        show = view.findViewById(R.id.btShowMenu)
        season = view.findViewById(R.id.btShowSeasonIcecream)

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
        val fragment = MenuSeasonFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction. replace(R.id.ly_principal, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
