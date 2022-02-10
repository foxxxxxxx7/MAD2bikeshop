package com.wit.mad2bikeshop.main

import android.app.Application
import com.wit.mad2bikeshop.model.BookMemStore
import com.wit.mad2bikeshop.model.BookStore
import timber.log.Timber

class BikeshopApp : Application() {

    lateinit var bookStore: BookStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        bookStore = BookMemStore()
        Timber.i("Starting Bikeshop Application")
    }
}