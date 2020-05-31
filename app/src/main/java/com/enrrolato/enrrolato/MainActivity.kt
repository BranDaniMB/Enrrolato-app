package com.enrrolato.enrrolato

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.internal.CallbackManagerImpl
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_main.*

enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class MainActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100;
    private val callbackManager = CallbackManager.Factory.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        // Cambiamos al tema por defecto
        setTheme(R.style.Theme_AppCompat_NoActionBar)
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

        callbackManager.onActivityResult(requestCode, resultCode, data)

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
            if (!usernameField.text.isNullOrEmpty() && !passwordField.text.isNullOrEmpty()) {
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

        facebookLoginBtn.setOnClickListener{

            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object: FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        result?.let {
                            val token = it.accessToken

                            val credential = FacebookAuthProvider.getCredential(token.token)
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        showMenu(it.result?.user?.email ?: "", ProviderType.FACEBOOK)
                                    } else {
                                        showAlert();
                                    }
                                }
                        }
                    }

                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException?) {
                        showAlert()
                    }
                })
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

            if (provider == ProviderType.FACEBOOK.name) {
                LoginManager.getInstance().logOut()
            }

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
        val menuIntent = Intent(this, PrincipalScreen::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider)
        }
        startActivity(menuIntent)
    }
}

