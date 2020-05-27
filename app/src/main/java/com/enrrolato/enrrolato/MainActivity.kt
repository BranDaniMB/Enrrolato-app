package com.enrrolato.enrrolato

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        // Session Verifica si ya se inicio sesión
        session()
    }

    override fun onStart() {
        super.onStart()

        loginLayout.visibility = View.VISIBLE
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            loginLayout.visibility = View.INVISIBLE
            showMenu(email, ProviderType.valueOf(provider))
        }
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
        /*loginBtn.setOnClickListener {
            // Borrar los datos guardados
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

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

