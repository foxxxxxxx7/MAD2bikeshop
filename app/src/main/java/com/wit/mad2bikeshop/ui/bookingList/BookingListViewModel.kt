package com.wit.mad2bikeshop.ui.bookingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.wit.mad2bikeshop.firebase.FirebaseDBManager
import com.wit.mad2bikeshop.model.BookManager
import com.wit.mad2bikeshop.model.BookModel
import timber.log.Timber
import java.lang.Exception

class BookingListViewModel : ViewModel() {

    private val bookingList = MutableLiveData<List<BookModel>>()

    val observableBookingList: LiveData<List<BookModel>>
        get() = bookingList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }

    fun findAll(): List<BookModel> {
        return BookManager.bookings
    }

    fun load() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            FirebaseDBManager.findAll(
                liveFirebaseUser.value?.uid!!,
                bookingList
            )
            Timber.i("Booking List Load Success : ${bookingList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Booking List Load Error : $e.message")
        }
    }

    fun del(userid: String, uid: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid, uid)
            Timber.i("Booking List Delete Success")
        } catch (e: Exception) {
            Timber.i("Booking List Delete Error : $e.message")
        }
    }
}