package com.wit.mad2bikeshop.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.wit.mad2bikeshop.firebase.FirebaseAuthManager

/* This is the view model for the logged in screen. It is responsible for getting the current user and
logging out. */
class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager: FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser: MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser
    var loggedOut: MutableLiveData<Boolean> = firebaseAuthManager.loggedOut

    fun logOut() {
        firebaseAuthManager.logOut()
    }
}