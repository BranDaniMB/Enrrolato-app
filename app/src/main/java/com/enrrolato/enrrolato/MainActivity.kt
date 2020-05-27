package com.enrrolato.enrrolato

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100;

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                showMenu(account.email ?: "", ProviderType.GOOGLE)
                            } else {
                                showAlert();
                            }
                        }
                }
            } catch (e: ApiException) {
                showAlert()
            }
        }
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

        // Google login
        googleLoginBtn.setOnClickListener{
            val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConfig)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
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

