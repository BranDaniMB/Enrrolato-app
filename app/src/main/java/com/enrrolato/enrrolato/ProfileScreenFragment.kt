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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.enrrolato.enrrolato.database.Enrrolato
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */

class ProfileScreenFragment : Fragment() {

    private var enrrolato = Enrrolato.instance
    private var auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)
        val btAbout = view.findViewById<View>(R.id.btAboutUs) as Button
        val btOut = view.findViewById<View>(R.id.btLogout) as Button
        val btRestore = view.findViewById<View>(R.id.btChangePassword) as Button
        val email = view.findViewById<View>(R.id.txtAssociatedUsername) as TextView

        showEmail(view)
        //email.text = enrrolato.getUsername(auth.currentUser?.email)

        btAbout.setOnClickListener {
            showAboutUsFragment()
        }

        btRestore.setOnClickListener {
            showRestoreFragment()
        }

        btOut.setOnClickListener {
            logOut()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showAboutUsFragment() {
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
        associedEmail.text = auth.currentUser?.email
    }

    private fun logOut() {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupConfirmExitView =
            layoutInflater.inflate(R.layout.popup_confirm_exit, null)
        val bt_ok: Button = popupConfirmExitView.findViewById(R.id.btOk);
        val bt_cancel: Button = popupConfirmExitView.findViewById(R.id.btCancel);
        alertDialogBuilder?.setView(popupConfirmExitView)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            fragmentManager!!.beginTransaction().remove(this).commit();
            alertDialog.cancel()

            val int: Intent = Intent(activity, MainActivity::class.java)
            startActivity(int)
            Toast.makeText(context, "Usted ha cerrado sesión", Toast.LENGTH_SHORT).show()
            val prefs = activity!!.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
    }
}
