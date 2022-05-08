package com.wit.mad2bikeshop.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

/* A function that takes in a MutableLiveData of a list of BookModel and returns nothing. */
interface BookStore {
    fun findAll(
        bookingsList:
        MutableLiveData<List<BookModel>>
    )

    /**
     * This function takes in a userid and a MutableLiveData of type List of BookModel and returns a
     * MutableLiveData of type List of BookModel
     *
     * @param userid The userid of the user whose bookings are to be fetched.
     * @param bookingsList This is the list of bookings that will be returned.
     */
    fun findAll(
        userid: String,
        bookingsList:
        MutableLiveData<List<BookModel>>
    )

    /**
     * It finds a booking by its id.
     *
     * @param userid The userid of the user who booked the ticket.
     * @param bookingid The id of the booking you want to find
     * @param booking MutableLiveData<BookModel>
     */
    fun findById(
        userid: String, bookingid: String,
        booking: MutableLiveData<BookModel>
    )

    /**
     *
     *
     * @param firebaseUser MutableLiveData<FirebaseUser> - This is the user that is currently logged
     * in.
     * @param booking BookModel is the model class for the booking.
     */
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, booking: BookModel)
    fun delete(userid: String, bookingid: String)
    fun update(userid: String, bookingid: String, booking: BookModel)
}
