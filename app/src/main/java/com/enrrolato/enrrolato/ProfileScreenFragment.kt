package com.enrrolato.enrrolato

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile_screen.*


/**
 * A simple [Fragment] subclass.
 */

  class ProfileScreenFragment : Fragment() {

    //val logOut = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)
        val btAbout = view.findViewById<View>(R.id.btAboutUs) as Button
        val btExit = view.findViewById<View>(R.id.btLogout) as Button
        val btRestore = view.findViewById<View>(R.id.btChangePassword) as Button

        showEmail(view)

        btAbout.setOnClickListener {
            showAbouUsFragment()
        }

        btRestore.setOnClickListener {
            showRestoreFragment()
        }

        btExit.setOnClickListener {
            logOut()
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

    private fun showRestoreFragment() {
        val fragment = RestorePasswordFragment()
        val fm = requireActivity().supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.ly_profile, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showEmail(view: View) {
            var associedEmail: TextView = view.findViewById(R.id.txtAssociatedEmail) as TextView
            associedEmail.setText(FirebaseAuth.getInstance().currentUser?.email)
    }

    private fun logOut() {
        val int: Intent = Intent(activity, MainActivity::class.java)
        startActivity(int)
        Toast.makeText(context, "Usted ha cerrado sesión", Toast.LENGTH_SHORT).show()

/*        if (logOut == true) {
            FirebaseAuth.getInstance().signOut()
            val i2 = requireActivity().Intent(this@ProfileScreenFragment, MainActivity::class.java)
            startActivity(i2)
            finish()
            return true */

//        val i1 = Intent(activity, MainActivity::class.java)
//        startActivity(i1)
//        Toast.makeText(ProfileScreenFragment.this, "Usted ha cerrado sesión", Toast.LENGTH_SHORT).show()
    }


}
