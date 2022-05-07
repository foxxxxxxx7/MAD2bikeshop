package com.wit.mad2bikeshop.ui.map

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import timber.log.Timber

@SuppressLint("MissingPermission")
class MapsViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var map : GoogleMap
    var currentLocation = MutableLiveData<Location>()
    var locationClient : FusedLocationProviderClient

    init {
        locationClient = LocationServices.getFusedLocationProviderClient(application)
    }

    fun updateCurrentLocation() {
        if(locationClient.lastLocation.isSuccessful)
            locationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    currentLocation.value = location!!
                }
        Timber.i("LOC : %s", currentLocation.value)
    }
}