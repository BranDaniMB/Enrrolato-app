package com.enrrolato.enrrolato

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_restore_password.*


class RestorePasswordFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()

    private lateinit var profile: ImageButton
    private lateinit var change: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_restore_password, container, false)
        profile = view.findViewById(R.id.btBackToProfile)
        change = view.findViewById(R.id.btRestore)

        profile.setOnClickListener {
            showProfileFragment()
        }

        change.setOnClickListener {
            sendEmail(view)
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

    private fun sendEmail(v: View) {
        val email: String = editEmail.text.toString().trim()
        auth.setLanguageCode("es")

        if(email.equals(auth.currentUser?.email) && email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    successful(v)
                    showProfileFragment()
                } else {
                    failure(v)
                }
            }
       } else {
            empty(v)
        }
    }

    private fun successful(v: View) {
        val toast: Toast =
            Toast.makeText(v.context, getString(R.string.reset_password_success), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

    private fun failure(v: View) {
        val toast: Toast =
            Toast.makeText(v.context, getString(R.string.reset_password_failure), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

    private fun empty(v: View) {
        val toast: Toast =
            Toast.makeText(v.context, getString(R.string.reset_password_auth_error), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }
}


