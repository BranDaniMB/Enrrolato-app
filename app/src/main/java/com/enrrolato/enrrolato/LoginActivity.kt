package com.enrrolato.enrrolato

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.enrrolato.enrrolato.database.Enrrolato
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100
    private val callbackManager = CallbackManager.Factory.create()
    private var enrrolato = Enrrolato.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        // CAMBIAMOS AL TEMA POR DEFECTO
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // ANALYTICS EVNT
        val analytics : FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Pantalla de inicio de sesión lanzada")
        analytics.logEvent("LoginScreen", bundle)

        setup()
        // SESSION = VERIFICA SI YA SE INICIÓ SESIÓN
        session()
        recoverPassword()
    }

    override fun onStart() {
        super.onStart()
        session()
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
                                var id: String? = FirebaseAuth.getInstance().currentUser?.uid
                                showMenu(id, account.displayName)
                            } else if (it.isCanceled) {

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
        var id = prefs.getString("id", null)
        val email = prefs.getString("email", null)
        val username = prefs.getString("username", null)
        //val provider = prefs.getString("provider", null)

        if (email != null) {
            if (id != null) {
                Enrrolato.instance.initUser(id,  username)
            }
            showMenu(id, username)
        }
    }

    private fun setup() {

        // Da error si la cuenta no esta registrada
        loginBtn.setOnClickListener {
            if (!emailField2.text.isNullOrEmpty() && !passwordField.text.isNullOrEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailField2.text.toString()
                    , passwordField.text.toString()).addOnCompleteListener {

                    var name = ""

                    if (it.isSuccessful) {
                        enrrolato.getUsername().addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(d: DataSnapshot) {
                                name = d.child("username").value.toString()
                            }
                            override fun onCancelled(d: DatabaseError) {
                            }
                        })
                        //var id: String? = FirebaseAuth.getInstance().currentUser?.uid

                        showMenu(it.result?.user?.uid, it.result?.user?.displayName?: name)
                    } else {
                        showAlert();
                    }
                }
            } else {
                Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT).show()
                //errorMessage("El email y/o contraseña están vacíos")
            }
        }

        // Google login
        val googleLogin:SignInButton = findViewById(R.id.googleLoginBtn)
        googleLogin.setSize(SignInButton.SIZE_WIDE);
        googleLogin.setOnClickListener{
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
                                        var id: String? = FirebaseAuth.getInstance().currentUser?.uid
                                        showMenu(id, it.result?.user?.displayName)
                                    } else {
                                        showAlert();
                                    }
                                }
                        }
                    }

                    override fun onCancel() {}

                    override fun onError(error: FacebookException?) {
                        showAlert()
                    }
                })
        }

        signUpBtn.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_title)
        builder.setMessage(R.string.log_in_alert)
        builder.setPositiveButton(R.string.accept_action, null)
        val dialog: AlertDialog =  builder.create()
        dialog.show()
    }

    private fun showMenu(id: String?, username: String?) {
        val menuIntent = Intent(this, PrincipalScreen::class.java)

        // Guardando la session
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("id", id)
        prefs.putString("username", username)
        //prefs.putString("email", email)
        //prefs.putString("provider", provider.name)
        prefs.apply()
        startActivity(menuIntent)
        finish()
    }

    private fun recoverPassword() {
        resetPassword.setOnClickListener {
            val recovery = Intent(this, ResetPassword::class.java)
            startActivity(recovery)
        }
    }

}

