package com.wit.mad2bikeshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.ActivityBookBinding
import com.wit.mad2bikeshop.main.BikeshopApp
import com.wit.mad2bikeshop.model.BookModel
import timber.log.Timber


class Book : AppCompatActivity() {

    private lateinit var bookLayout: ActivityBookBinding
    lateinit var app: BikeshopApp
    var booking = BookModel()
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = this.application as BikeshopApp
        bookLayout = ActivityBookBinding.inflate(layoutInflater)
        setContentView(bookLayout.root)

        bookLayout.bookButton.setOnClickListener() {
            booking.name = bookLayout.bookName.text.toString()
            booking.phoneNumber = bookLayout.bookNumber.text.toString()
            booking.email = bookLayout.bookEmail.text.toString()
            booking.pickup = bookLayout.bookPickup.text.toString()
            booking.dropoff = bookLayout.bookDropoff.text.toString()
            if (booking.name.isEmpty() || booking.phoneNumber.isEmpty() || booking.email.isEmpty() || booking.pickup.isEmpty() || booking.dropoff.isEmpty()) {
                /*Timber.i(R.string.enter_details)*/
            } else {
                if (edit) {
                    app.bookStore.update(booking.copy())
                } else {
                    app.bookStore.create(booking.copy())
                }
            }
            /* info("Add Button Pressed: $bookLayout.bookName, $bookLayout.bookNumber, $bookLayout.bookEmail")
            setResult(AppCompatActivity.RESULT_OK)*/
            finish()
        }

        /*  toolbarAdd.name = name
        setSupportActionBar(toolbarAdd)
    }*/
    }
}