package com.wit.mad2bikeshop.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.model.BookStore
import timber.log.Timber

var database: DatabaseReference = FirebaseDatabase.getInstance("https://viking-bike-hire-13ed9-default-rtdb.europe-west1.firebasedatabase.app").getReference()

object FirebaseDBManager : BookStore {
    override fun findAll(bookingsList: MutableLiveData<List<BookModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, bookingsList: MutableLiveData<List<BookModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(userid: String, bookingid: String, booking: MutableLiveData<BookModel>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, booking: BookModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("bookings").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        booking.uid = key
        val bookingValues = booking.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/bookings/$key"] = bookingValues
        childAdd["/user-bookings/$uid/$key"] = bookingValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, bookingid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, bookingid: String, booking: BookModel) {
        TODO("Not yet implemented")
    }
}