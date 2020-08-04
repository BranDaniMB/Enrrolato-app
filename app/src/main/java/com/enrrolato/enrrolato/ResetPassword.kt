package com.enrrolato.enrrolato

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        btRecovery.setOnClickListener {
            sendEmail()
        }
    }

    private fun sendEmail() {
        val email: String = emailToRecovery.text.toString().trim()
        auth.setLanguageCode("es")

        if(!email.isEmpty()) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    successful()
                    goToBack()
                } else {
                    failure()
                }
            }
        } else {
            empty()
        }
    }

    private fun goToBack() {
        val back = Intent(this, LoginActivity::class.java)
        startActivity(back)
    }

    private fun successful() {
        val toast: Toast =
            Toast.makeText(this, getString(R.string.reset_password_success), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

    private fun failure() {
        val toast: Toast =
            Toast.makeText(this, getString(R.string.reset_password_failure), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

    private fun empty() {
        val toast: Toast =
            Toast.makeText(this, getString(R.string.reset_password_empty), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

}