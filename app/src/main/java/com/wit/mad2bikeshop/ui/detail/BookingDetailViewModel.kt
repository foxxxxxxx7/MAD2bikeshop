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

    val observableBooking: LiveData<BookModel>
        get() = booking

    fun getBooking(email: String, id: String) {
        try {
            BookManager.findById(email, id, booking)
            Timber.i("Detail getBooking() Success : ${booking.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Detail getBooking() Error : $e.message")
        }
    }

    fun updateBook(userid:String, id: String,booking: BookModel) {
        try {
            //BookManager.update(email, id, booking)
            FirebaseDBManager.update(userid, id, booking)
            Timber.i("Detail update() Success : $booking")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}