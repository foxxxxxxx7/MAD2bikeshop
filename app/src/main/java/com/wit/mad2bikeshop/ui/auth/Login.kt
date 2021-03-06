package com.wit.mad2bikeshop.ui.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wit.mad2bikeshop.R

import com.wit.mad2bikeshop.utils.createLoader
import com.wit.mad2bikeshop.databinding.LoginBinding
import com.wit.mad2bikeshop.ui.home.Home
import com.wit.mad2bikeshop.utils.hideLoader
import com.wit.mad2bikeshop.utils.showLoader

import timber.log.Timber


/* This is the code for the login screen. */
class Login : AppCompatActivity() {

    private lateinit var loginRegisterViewModel: LoginRegisterViewModel
    private lateinit var loginBinding: LoginBinding
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = LoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.emailSignInButton.setOnClickListener {
            signIn(
                loginBinding.fieldEmail.text.toString(),
                loginBinding.fieldPassword.text.toString()
            )
        }
        loginBinding.emailCreateAccountButton.setOnClickListener {
            createAccount(
                loginBinding.fieldEmail.text.toString(),
                loginBinding.fieldPassword.text.toString()
            )
        }

        loginBinding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)
        loginBinding.googleSignInButton.setColorScheme(0)

        loginBinding.googleSignInButton.setOnClickListener {
            googleSignIn()
        }
    }

    /**
     * We're launching the Google sign in intent, which will open the Google sign in activity
     */
    private fun googleSignIn() {
        val signInIntent = loginRegisterViewModel.firebaseAuthManager
            .googleSignInClient.value!!.signInIntent

        startForResult.launch(signInIntent)
    }

    /**
     * The function observes the liveFirebaseUser object in the loginRegisterViewModel. If the user is
     * not null, the user is redirected to the Home activity. If the user is null, the user is not
     * redirected
     */
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginRegisterViewModel = ViewModelProvider(this).get(LoginRegisterViewModel::class.java)
        loginRegisterViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser ->
            if (firebaseUser != null)
                startActivity(Intent(this, Home::class.java))
        })

        loginRegisterViewModel.firebaseAuthManager.errorStatus.observe(this, Observer
        { status -> checkStatus(status) })

        setupGoogleSignInCallback()
    }

    /**
     * A function that is used to setup the Google Sign In callback.
     */
    private fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            loginRegisterViewModel.authWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                            Timber.i("Google sign in failed $e")
                            Snackbar.make(
                                loginBinding.loginLayout, "Authentication Failed.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        Timber.i("DonationX Google Result $result.data")
                    }
                    RESULT_CANCELED -> {

                    }
                    else -> {}
                }
            }
    }


    /**
     * If the back button is pressed, the app will close
     */
    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Click again to Close App...", Toast.LENGTH_LONG).show()
        finish()
    }

    /**
     * It creates an account for the user.
     *
     * @param email The email address of the user.
     * @param password The password for the new account.
     * @return A boolean value
     */
    private fun createAccount(email: String, password: String) {
        Timber.d("createAccount:$email")
        if (!validateForm()) {
            return
        }

        loginRegisterViewModel.register(email, password)
    }

    /**
     * The function `signIn` takes two parameters, `email` and `password`, and if the form is valid, it
     * calls the `login` function in the `LoginRegisterViewModel` class
     *
     * @param email The email address of the user.
     * @param password The user's password.
     * @return A boolean value
     */
    private fun signIn(email: String, password: String) {
        Timber.d("signIn:$email")
        if (!validateForm()) {
            return
        }

        loginRegisterViewModel.login(email, password)
    }

    /**
     * If the error is true, show a toast message
     *
     * @param error Boolean - true if the authentication failed, false if it succeeded
     */
    private fun checkStatus(error: Boolean) {
        if (error)
            Toast.makeText(
                this,
                getString(R.string.auth_failed),
                Toast.LENGTH_LONG
            ).show()
    }

    /**
     * If the email and password fields are not empty, then return true
     *
     * @return A boolean value.
     */
    private fun validateForm(): Boolean {
        var valid = true

        val email = loginBinding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            loginBinding.fieldEmail.error = "Required."
            valid = false
        } else {
            loginBinding.fieldEmail.error = null
        }

        val password = loginBinding.fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            loginBinding.fieldPassword.error = "Required."
            valid = false
        } else {
            loginBinding.fieldPassword.error = null
        }
        return valid
    }


}