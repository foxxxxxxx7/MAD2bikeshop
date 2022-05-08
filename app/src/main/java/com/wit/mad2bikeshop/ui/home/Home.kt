package com.wit.mad2bikeshop.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.HomeBinding
import com.wit.mad2bikeshop.databinding.NavHeaderBinding
import com.wit.mad2bikeshop.firebase.FirebaseImageManager
import com.wit.mad2bikeshop.ui.auth.LoggedInViewModel
import com.wit.mad2bikeshop.ui.auth.Login
import com.wit.mad2bikeshop.ui.map.MapsViewModel
import com.wit.mad2bikeshop.utils.*
import timber.log.Timber

/* The above code is declaring the variables that will be used in the Home activity. */
class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding: HomeBinding
    private lateinit var navHeaderBinding: NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel: LoggedInViewModel
    private lateinit var headerView: View
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>
    private val mapsViewModel: MapsViewModel by viewModels()

    /**
     * The onCreate function of the HomeActivity. It sets up the navigation drawer and the toolbar.
     *
     * @param savedInstanceState The Bundle that contains the most recently saved state of this
     * activity,
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.bookFragment, R.id.bookingListFragment, R.id.mapsFragment, R.id.aboutFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)
        initNavHeader()

        if (checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }
    }

    /**
     * If the user has granted the permission, update the current location, otherwise use a default
     * location
     *
     * @param requestCode The request code passed in requestPermissions(android.app.Activity, String[],
     * int)
     * @param permissions The permissions that were requested.
     * @param grantResults An array of the results of each permission
     */
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            mapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            mapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.260791
                longitude = -7.105922
            }
        }
        Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
    }

    /**
     * The function observes the loggedInViewModel's liveFirebaseUser and loggedOut variables. If the
     * liveFirebaseUser variable is not null, the function calls the updateNavHeader function. If the
     * loggedOut variable is true, the function starts the Login activity
     */
    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(firebaseUser)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })
        registerImagePickerCallback()
    }

    /**
     * > We get the header view from the navigation view, bind it to a NavHeaderBinding object, and set
     * an onClickListener on the image view
     */
    private fun initNavHeader() {
        Timber.i("DX Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)

        navHeaderBinding.navHeaderImage.setOnClickListener {
            showImagePicker(intentLauncher)
        }
    }

    /**
     * If the user has an image stored in Firebase, load it. If not, load the default image
     *
     * @param currentUser FirebaseUser - the current user
     */
    private fun updateNavHeader(currentUser: FirebaseUser) {
        FirebaseImageManager.imageUri.observe(this, { result ->
            if (result == Uri.EMPTY) {
                Timber.i("DX NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        navHeaderBinding.navHeaderImage,
                        false
                    )
                } else {
                    Timber.i("DX Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.ic_book_nav_header,
                        navHeaderBinding.navHeaderImage
                    )
                }
            } else // load existing image from firebase
            {
                Timber.i("DX Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    navHeaderBinding.navHeaderImage, false
                )
            }
        })
        navHeaderBinding.navHeaderEmail.text = currentUser.email
        if (currentUser.displayName != null)
            navHeaderBinding.navHeaderName.text = currentUser.displayName
    }

    /**
     * If the user presses the back button, the app will navigate to the previous fragment
     *
     * @return The return value is a boolean.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    /**
     * When the user clicks the sign out button, the app logs the user out and launches the Login
     * activity
     *
     * @param item MenuItem - The menu item that was clicked
     */
    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    /**
     * It registers a callback for the image picker.
     */
    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i(
                                "DX registerPickerCallback() ${
                                    readImageUri(
                                        result.resultCode,
                                        result.data
                                    ).toString()
                                }"
                            )
                            FirebaseImageManager
                                .updateUserImage(
                                    loggedInViewModel.liveFirebaseUser.value!!.uid,
                                    readImageUri(result.resultCode, result.data),
                                    navHeaderBinding.navHeaderImage,
                                    true
                                )
                        } // end of if
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
}

