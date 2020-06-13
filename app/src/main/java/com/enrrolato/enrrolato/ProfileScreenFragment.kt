package com.enrrolato.enrrolato

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
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */

  class ProfileScreenFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile_screen, container, false)
        val btAbout = view.findViewById<View>(R.id.btAboutUs) as Button
        val btOut = view.findViewById<View>(R.id.btLogout) as Button
        val btRestore = view.findViewById<View>(R.id.btChangePassword) as Button
        showEmail(view)

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
            var associedEmail: TextView = view.findViewById(R.id.txtAssociatedEmail) as TextView
            associedEmail.setText(FirebaseAuth.getInstance().currentUser?.email)
    }

    private fun logOut() {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it, R.style.alert_dialog) }
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        val popupConfirmExitView = layoutInflater.inflate(R.layout.activity_popup_confirm_exit, null)
        val bt_ok: Button = popupConfirmExitView.findViewById(R.id.btOk);
        val bt_cancel: Button = popupConfirmExitView.findViewById(R.id.btCancel);
        alertDialogBuilder?.setView(popupConfirmExitView)
        val alertDialog: AlertDialog = alertDialogBuilder!!.create()
        alertDialog.window?.attributes!!.windowAnimations = R.style.alert_dialog
        alertDialog.show()

        bt_ok.setOnClickListener {
            fragmentManager!!.beginTransaction()!!.remove(this).commit();
            alertDialog.cancel()

            val int: Intent = Intent(activity, MainActivity::class.java)
            startActivity(int)
            Toast.makeText(context, "Usted ha cerrado sesión", Toast.LENGTH_SHORT).show()
        }

        bt_cancel.setOnClickListener {
            alertDialog.cancel()
        }
/*
*  LayoutInflater layoutInflater = LayoutInflater.from(ExamActivity.this);
            View popupInputDialogView = layoutInflater.inflate(R.layout.popup_exit_exam, null);
            Button bt_save = popupInputDialogView.findViewById(R.id.bt_save);
            Button bt_cancel = popupInputDialogView.findViewById(R.id.bt_cancel);
            alertDialogBuilder.setView(popupInputDialogView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.alert_dialog;
            alertDialog.show();
            bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExamActivity.this.finish();
                    alertDialog.cancel();
                }
            });
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
* */

    }
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
