package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class RestorePasswordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_restore_password, container, false)
        val btProfile = view.findViewById<View>(R.id.btBackToProfile) as ImageButton

        btProfile.setOnClickListener {
            showProfileFragment()
        }

        return view
    }

    private fun showProfileFragment() {
        val fragment = ProfileScreenFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_restore_pass, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}