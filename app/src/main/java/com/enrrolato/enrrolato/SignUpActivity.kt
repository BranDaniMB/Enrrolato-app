package com.enrrolato.enrrolato

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.database.Enrrolato
import com.enrrolato.enrrolato.database.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private var enrrolato = Enrrolato.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setup()
    }

    private fun setup() {
        registerBtn.setOnClickListener {
            if (!emailField.text.isNullOrEmpty() || !usernameField.text.isNullOrEmpty() && !newPasswordField.text.isNullOrEmpty() && !repeatPasswordField.text.isNullOrEmpty()) {
                if (newPasswordField.text.toString() == repeatPasswordField.text.toString()) {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailField.text.toString()
                        , newPasswordField.text.toString()).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showLogin()
                            enrrolato.initUser(enrrolato.getId(), usernameField.text.toString())
                        }
                        else {
                            signUpAlert()
                        }
                    }
                } else {
                    differentPasswords()
                }
            }
        }
    }

    private fun differentPasswords() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(R.string.different_passwords)
        builder.setPositiveButton(R.string.accept_action, null)
        val dialog: AlertDialog =  builder.create()
        dialog.show()
    }

    private fun signUpAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(R.string.sign_up_alert)
        builder.setPositiveButton(R.string.accept_action, null)
        val dialog: AlertDialog =  builder.create()
        dialog.show()
    }

    private fun showLogin() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        Toast.makeText(baseContext, getString(R.string.sign_up_message), Toast.LENGTH_LONG).show()
        finish()
    }
}