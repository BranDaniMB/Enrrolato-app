package com.enrrolato.enrrolato

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_profile_screen.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        /*btAboutUs.setOnClickListener {
            var goToAbout = getFragmentManager()?.beginTransaction()
            goToAbout?.replace(R.layout.fragment_profile_screen, R.layout.fragment_about_us)
            goToAbout?.commit() }*/



        return inflater.inflate(R.layout.fragment_profile_screen, container, false)
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
