package com.enrrolato.enrrolato.createIcecream

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.enrrolato.enrrolato.PrincipalMenuFragment
import com.enrrolato.enrrolato.R
import com.enrrolato.enrrolato.RestorePasswordFragment

class FlavorsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_flavors, container, false)
        val back = view.findViewById<View>(R.id.btBackToPrincipal) as ImageButton

        back.setOnClickListener {
            backToPrincipal()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun backToPrincipal() {
        val fragment = PrincipalMenuFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_flavor, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun nextStep() {
        // CODE HERE
    }

    private fun chargeFlavors() {
        // CODE HERE
    }

    private fun flavorProcess() {
        // CODE HERE
    }

    private fun filtrer() {
        // CODE HERE
    }

}