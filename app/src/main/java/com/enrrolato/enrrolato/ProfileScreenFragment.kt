package com.enrrolato.enrrolato

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.enrrolato.enrrolato.database.Enrrolato
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

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
        val username = view.findViewById<View>(R.id.txtAssociatedUsername) as TextView
        val editUsername = view.findViewById<View>(R.id.btEditUsername) as ImageButton
        val hide = view.findViewById<View>(R.id.txtHidden) as TextView

        showEmail(view)
        enrrolato.getUsername().addValueEventListener(object : ValueEventListener{
            override fun onDataChange(d: DataSnapshot) {
                username.text = d.child("username").value.toString()
            }

            override fun onCancelled(d: DatabaseError) {
            }

        })

        btAbout.setOnClickListener {
            showAboutUsFragment()
        }

        btRestore.setOnClickListener {
            showRestoreFragment()
        }

        btOut.setOnClickListener {
            logOut()
        }

        editUsername.setOnClickListener {
            updateName(hide)
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

            val int: Intent = Intent(activity, LoginActivity::class.java)
            startActivity(int)
            Toast.makeText(context, getString(R.string.log_out_message), Toast.LENGTH_SHORT).show()
            val prefs = activity!!.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
    }

    private fun updateName(hide: TextView) {
        var txt = "Ingrese su nuevo nombre"
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupUsername = layoutInflater.inflate(R.layout.popup_username, null)
        val msg: TextView = popupUsername.findViewById(R.id.txtMessage)
        msg.text = txt
        val bt_ok: Button = popupUsername.findViewById(R.id.btOkUsername);
        val bt_cancel: Button = popupUsername.findViewById(R.id.btCancelUsername);
        val u: TextView = popupUsername.findViewById(R.id.eTextUsername)
        alertDialogBuilder?.setView(popupUsername)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            hide.text = u.text.toString().trim()
            enrrolato.setUsername(hide.text.toString())
            alertDialog.cancel()
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
    }

}
