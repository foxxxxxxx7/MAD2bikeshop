package com.wit.mad2bikeshop.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object BookManager : BookStore {

    val bookings = ArrayList<BookModel>()

    override fun findAll(bookingsList: MutableLiveData<List<BookModel>>) {
        //return bookings
    }

    override fun findAll(userid: String, bookingsList: MutableLiveData<List<BookModel>>) {
       // return bookingsList
    }

    override fun findById(userid: String, bookingid: String, booking: MutableLiveData<BookModel>) {
        val foundBook: BookModel? = bookings.find { it.uid == userid }
        //return foundBook
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, booking: BookModel) {
        booking.uid = getId().toString()
        bookings.add(booking)
        logAll()
    }

    fun logAll() {
        Timber.v("** Bookings List **")
        bookings.forEach { Timber.v("Book ${it}") }
    }

    override fun delete(userid: String, bookingid: String) {
      //  bookings.remove(bookingid)
    }

    override fun update(userid: String, bookingid: String, booking: BookModel) {
        val foundBook: BookModel? = bookings.find { p -> p.uid == booking.uid }
        if (foundBook != null) {
            foundBook.date = booking.date
            foundBook.name = booking.name
            foundBook.phoneNumber = booking.phoneNumber
            foundBook.email = booking.email
            foundBook.pickup = booking.pickup
            foundBook.dropoff = booking.dropoff
            foundBook.price = booking.price
            /*foundBook.image = bike.image
            foundBook.lat = bike.lat
            foundBook.lng = bike.lng
            foundBook.zoom = bike.zoom*/
            logAll()
        }
    }

}