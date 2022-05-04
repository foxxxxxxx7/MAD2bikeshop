package com.wit.mad2bikeshop.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wit.mad2bikeshop.firebase.FirebaseDBManager
import com.wit.mad2bikeshop.model.BookManager
import com.wit.mad2bikeshop.model.BookModel
import timber.log.Timber
import java.lang.Exception

class BookingDetailViewModel : ViewModel() {
    private val booking = MutableLiveData<BookModel>()

    var observableBooking: LiveData<BookModel>
        get() = booking
        set(value) {booking.value = value.value}

    fun getBooking(userid:String, id: String) {
        try {
            //BookManager.findById(email, id, donation)
            FirebaseDBManager.findById(userid, id, booking)
            Timber.i("Detail getBooking() Success : ${
                booking.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getBooking() Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Booking Deleted ")
        }
        catch (e: Exception) {
            Timber.i("Booking Delete Error : $e.message")
        }
    }

    fun updateBook(userid:String, id: String,booking: BookModel) {
        try {
            FirebaseDBManager.update(userid, id, booking)
            Timber.i("Detail update() Success : $booking")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}