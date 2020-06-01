package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * A simple [Fragment] subclass.
 */

  class ProfileScreenFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)
        val btAbout = view.findViewById<View>(R.id.btAboutUs) as Button

        btAbout.setOnClickListener {
            showAbouUsFragment()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showAbouUsFragment() {
        val fragment = AboutUsFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_profile, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /*private fun replaceFragment(fragment: AboutUsFragment) {
        val fragmentTransaction = getFragmentManager()?.beginTransaction()
        fragmentTransaction?.replace(R.id.ly_profile, fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }*/

            //val intent = Intent(this, AboutUsFragment::class.java)
            //startActivity(intent)


}
