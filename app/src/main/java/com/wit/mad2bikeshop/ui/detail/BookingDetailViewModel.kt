package com.wit.mad2bikeshop.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wit.mad2bikeshop.firebase.FirebaseDBManager
import com.wit.mad2bikeshop.model.BookManager
import com.wit.mad2bikeshop.model.BookModel
import timber.log.Timber
import java.lang.Exception

/* The BookingDetailViewModel class is a ViewModel class that holds a MutableLiveData object of type
BookModel */
class BookingDetailViewModel : ViewModel() {
    private val booking = MutableLiveData<BookModel>()

    var observableBooking: LiveData<BookModel>
        get() = booking
        set(value) {
            booking.value = value.value
        }

    /**
     * It gets the booking details from the database.
     *
     * @param userid The user's id
     * @param id The id of the booking you want to retrieve
     */
    fun getBooking(userid: String, id: String) {
        try {
            //BookManager.findById(email, id, donation)
            FirebaseDBManager.findById(userid, id, booking)
            Timber.i(
                "Detail getBooking() Success : ${
                    booking.value.toString()
                }"
            )
        } catch (e: Exception) {
            Timber.i("Detail getBooking() Error : $e.message")
        }
    }

    /**
     * It deletes a booking from the database.
     *
     * @param userid The userid of the user who created the booking.
     * @param id The id of the booking you want to delete.
     */
    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid, id)
            Timber.i("Booking Deleted ")
        } catch (e: Exception) {
            Timber.i("Booking Delete Error : $e.message")
        }
    }

    /**
     * It updates the book with the given id.
     *
     * @param userid The user's ID.
     * @param id The id of the book you want to update.
     * @param booking BookModel
     */
    fun updateBook(userid: String, id: String, booking: BookModel) {
        try {
            FirebaseDBManager.update(userid, id, booking)
            Timber.i("Detail update() Success : $booking")
        } catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}