package com.wit.mad2bikeshop.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import timber.log.Timber

/* A constant value that is used to identify the request code for the permission request. */
const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

/**
 * If the app has the location permission, return true. Otherwise, request the permission and return
 * false
 *
 * @param activity The activity that is requesting the permission.
 * @return A boolean value.
 */
fun checkLocationPermissions(activity: Activity): Boolean {
    return if (ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        true
    } else {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
        false
    }
}

/**
 * If the user granted the permission, return true, otherwise return false
 *
 * @param code The request code you passed to requestPermissions().
 * @param grantResults An array of the grant results for the corresponding permissions which is either
 * PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
 * @return A boolean value.
 */
fun isPermissionGranted(code: Int, grantResults: IntArray): Boolean {
    var permissionGranted = false;
    if (code == REQUEST_PERMISSIONS_REQUEST_CODE) {
        when {
            grantResults.isEmpty() -> Timber.i("User interaction was cancelled.")
            (grantResults[0] == PackageManager.PERMISSION_GRANTED) -> {
                permissionGranted = true
                Timber.i("Permission Granted.")
            }
            else -> Timber.i("Permission Denied.")
        }
    }
    return permissionGranted
}