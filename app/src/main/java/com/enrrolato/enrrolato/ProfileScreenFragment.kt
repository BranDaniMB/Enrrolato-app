package com.enrrolato.enrrolato

import android.content.Context
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

import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.database.ProviderType
import com.facebook.login.LoginManager

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
        val associedEmail: TextView = view.findViewById(R.id.txtAssociatedEmail) as TextView
        associedEmail.text = FirebaseAuth.getInstance().currentUser?.email
    }

    private fun logOut() {
        val int: Intent = Intent(activity, MainActivity::class.java)
        this.activity?.let {
            val prefs = it.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
        }
        if (Enrrolato.provider == ProviderType.FACEBOOK) {
            LoginManager.getInstance().logOut()
        }
        FirebaseAuth.getInstance().signOut()
        startActivity(int)
        Toast.makeText(context, getString(R.string.log_out_message), Toast.LENGTH_LONG).show()
    }


}
