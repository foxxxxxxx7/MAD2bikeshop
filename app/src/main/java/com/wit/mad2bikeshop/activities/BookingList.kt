package com.wit.mad2bikeshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.adapters.BookAdapter
import com.wit.mad2bikeshop.databinding.ActivityBookingListBinding
import com.wit.mad2bikeshop.main.BikeshopApp

lateinit var app: BikeshopApp
lateinit var booklistLayout : ActivityBookingListBinding

class BookingList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        booklistLayout = ActivityBookingListBinding.inflate(layoutInflater)
        setContentView(booklistLayout.root)

        app = this.application as BikeshopApp
        booklistLayout.recyclerView.layoutManager = LinearLayoutManager(this)
        booklistLayout.recyclerView.adapter = BookAdapter(app.bookStore.findAll())
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_booking_list, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_book -> { startActivity(Intent(this, Book::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}