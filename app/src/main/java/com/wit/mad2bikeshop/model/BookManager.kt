package com.wit.mad2bikeshop.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.wit.mad2bikeshop.firebase.FirebaseDBManager
import com.wit.mad2bikeshop.firebase.database
import timber.log.Timber
import java.lang.Exception

/**
 * It returns a unique ID
 *
 * @return A Long
 */
var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

/* Creating a singleton object. */
object BookManager : BookStore {

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    val bookings = ArrayList<BookModel>()

    /**
     * It returns a list of bookings.
     *
     * @param bookingsList MutableLiveData<List<BookModel>>
     */
    override fun findAll(bookingsList: MutableLiveData<List<BookModel>>) {
        //return bookings
    }

    /**
     * We are getting the userid from the user, then we are getting the bookings from the database,
     * then we are adding the bookings to the local list, then we are removing the event listener, then
     * we are setting the value of the bookings list to the local list
     *
     * @param userid The user id of the user whose bookings are to be retrieved.
     * @param bookingsList MutableLiveData<List<BookModel>>
     */
    override fun findAll(userid: String, bookingsList: MutableLiveData<List<BookModel>>) {

        database.child("user-bookings").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Booking error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<BookModel>()
                    val children = snapshot.children
                    children.forEach {
                        val booking = it.getValue(BookModel::class.java)
                        localList.add(booking!!)
                    }
                    database.child("user-bookings").child(userid)
                        .removeEventListener(this)

                    bookingsList.value = localList
                }
            })
    }

    /**
     * It finds a book by its id.
     *
     * @param userid The user's id
     * @param bookingid The id of the booking to be found
     * @param booking MutableLiveData<BookModel>
     */
    override fun findById(userid: String, bookingid: String, booking: MutableLiveData<BookModel>) {
        val foundBook: BookModel? = bookings.find { it.uid == userid }
        //return foundBook
    }

    /**
     * It adds a booking to the list of bookings.
     *
     * @param firebaseUser MutableLiveData<FirebaseUser>
     * @param booking BookModel - This is the booking object that you want to create.
     */
    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, booking: BookModel) {
        booking.uid = getId().toString()
        bookings.add(booking)
        logAll()
    }

    /**
     * > The function `logAll()` iterates over the `bookings` list and logs each booking
     */
    fun logAll() {
        Timber.v("** Bookings List **")
        bookings.forEach { Timber.v("Book ${it}") }
    }

    /**
     * It deletes a booking from the database.
     *
     * @param userid The userid of the user who made the booking
     * @param bookingid The id of the booking to be deleted
     */
    override fun delete(userid: String, bookingid: String) {
        //  bookings.remove(bookingid)
    }

    /**
     * The function takes in a userid, bookingid and a booking object. It then searches for the booking
     * object in the bookings array and if it finds it, it updates the booking object with the new
     * values
     *
     * @param userid The user's ID
     * @param bookingid The id of the booking to update
     * @param booking BookModel - this is the booking object that is being updated
     */
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