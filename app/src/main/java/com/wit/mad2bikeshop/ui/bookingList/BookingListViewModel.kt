package com.wit.mad2bikeshop.ui.bookingList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.wit.mad2bikeshop.firebase.FirebaseDBManager
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.ui.auth.LoggedInViewModel
import timber.log.Timber
import java.lang.Exception

/* This is the ViewModel for the BookingListActivity. It is responsible for loading the bookings from
the database and displaying them in the RecyclerView. */
class BookingListViewModel : ViewModel() {

    private var bookingsList = MutableLiveData<List<BookModel>>()

    val observableBookingList: LiveData<List<BookModel>>
        get() = bookingsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    val user = FirebaseAuth.getInstance().currentUser
    var readOnly = MutableLiveData(false)
    //var  ussser = LoggedInViewModel.liveFirebaseUser


  /* Calling the load function when the view model is created. */
      init {
        load()
    }

    // fun findAll(): List<BookModel> {
    //     return BookManager.bookings
    // }

    /**
     * It loads the bookings from the Firebase database.
     */
    fun load() {
        try {
            readOnly.value = false
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            Timber.i("Booking List 1")
            //Timber.i(liveFirebaseUser.value?.uid!!)
            Timber.i(user?.uid!!)
            //Timber.i(bookingsList.toString())
            FirebaseDBManager.findAll(
//                liveFirebaseUser.value?.uid!!,
                user?.uid!!,
                // "3kl1HSOCtVa7gLexgdnDgmzhRun1",
                bookingsList
            )
            Timber.i("Booking List Load Success : ${bookingsList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Booking List Load Error : $e.message")
        }
    }

    /**
     * It deletes a booking from the database.
     *
     * @param userid The userid of the user who is logged in.
     * @param id The id of the booking
     */
    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid, id)
            Timber.i("Booking Deleted ")
        } catch (e: Exception) {
            Timber.i("Booking Delete Error : $e.message")
        }
    }

    /**
     * It deletes a booking from the database.
     *
     * @param userid The user's ID.
     * @param uid The unique id of the booking list
     */
    fun del(userid: String, uid: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid, uid)
            Timber.i("Booking List Delete Success")
        } catch (e: Exception) {
            Timber.i("Booking List Delete Error : $e.message")
        }
    }

    /**
     * The function is called loadAll() and it's a public function that takes no parameters and returns
     * nothing
     */
    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(bookingsList)
            Timber.i("Booking List LoadAll Success : ${bookingsList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Booking List LoadAll Error : $e.message")
        }
    }
}