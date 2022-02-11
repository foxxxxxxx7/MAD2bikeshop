package com.wit.mad2bikeshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.ActivityBookBinding
import com.wit.mad2bikeshop.main.BikeshopApp
import com.wit.mad2bikeshop.model.BookModel
import timber.log.Timber
import timber.log.Timber.*
import android.widget.Toast


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
            print("Add Button Pressed: $bookLayout.bookName, $bookLayout.bookNumber, $bookLayout.bookEmail")
            setResult(AppCompatActivity.RESULT_OK)
            if (edit){
                finish()
            }
            /*finish()*/
        }


       /* toolbarAdd.name = name
        setSupportActionBar(toolbarAdd)*/
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_book, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_bookinglist -> { startActivity(Intent(this, BookingList::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}