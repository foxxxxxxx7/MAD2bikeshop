package com.wit.mad2bikeshop.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.wit.mad2bikeshop.firebase.FirebaseAuthManager

/* This is the ViewModel class for the LoginRegisterActivity. It is responsible for handling the data
and logic for the LoginRegisterActivity. */
class LoginRegisterViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager: FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser: MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser

    /**
     * It logs in a user with the given email and password.
     *
     * @param email The email of the user
     * @param password The password of the user.
     */
    fun login(email: String?, password: String?) {
        firebaseAuthManager.login(email, password)
    }

    /**
     * It registers a user with the given email and password.
     *
     * @param email The email address of the user.
     * @param password String?
     */
    fun register(email: String?, password: String?) {
        firebaseAuthManager.register(email, password)
    }

    /**
     * This function takes a GoogleSignInAccount object as a parameter and calls the
     * firebaseAuthWithGoogle() function in the FirebaseAuthManager class
     *
     * @param acct GoogleSignInAccount
     */
    fun authWithGoogle(acct: GoogleSignInAccount) {
        firebaseAuthManager.firebaseAuthWithGoogle(acct)
    }
}