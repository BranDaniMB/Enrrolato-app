package com.enrrolato.enrrolato

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Analytics Event
        val analytics : FirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        val bundle = Bundle()
        bundle.putString("message", "Pantalla de inicio de sesión lanzada")
        analytics.logEvent("LoginScreen", bundle)

        // Setup
        setup()
    }

    private fun setup() {

        // Da error si la cuenta no esta registrada
        loginBtn.setOnClickListener {
            if (usernameField.text.isNullOrEmpty() && passwordField.text.isNullOrEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(usernameField.text.toString()
                    , passwordField.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showMenu(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert();
                    }
                }
            }
        }

        // Para crear la cuenta, da error si ya esta creada.
        /*loginBtn.setOnClickListener {
            if (usernameField.text.isNullOrEmpty() && passwordField.text.isNullOrEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(usernameField.text.toString()
                    , passwordField.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showMenu(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert();
                    }
                }
            }
        }*/

        // Para cerrar sesión
        /*loginBtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }*/
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog =  builder.create();
        dialog.show();
    }

    private fun showMenu(email: String, provider: ProviderType) {
        val menuIntent = Intent(this, MenuScreen::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider)
        }
        startActivity(menuIntent)
    }
}

