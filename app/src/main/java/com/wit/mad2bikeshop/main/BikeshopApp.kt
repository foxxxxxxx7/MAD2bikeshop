package com.wit.mad2bikeshop.main

import android.app.Application
import timber.log.Timber

class BikeshopApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Starting Bikeshop Application")
    }
}