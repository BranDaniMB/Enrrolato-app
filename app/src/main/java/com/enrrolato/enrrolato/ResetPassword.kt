package com.enrrolato.enrrolato

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.fragment_restore_password.*

class ResetPassword : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()

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
        val back = Intent(this, MainActivity::class.java)
        startActivity(back)
    }

    private fun successful() {
        val toast: Toast =
            Toast.makeText(this, "Se envió el correo correctamente", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

    private fun failure() {
        val toast: Toast =
            Toast.makeText(this, "El correo no se pudo enviar", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

    private fun empty() {
        val toast: Toast =
            Toast.makeText(this, "El campo de texto se encuentra vacío", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

}