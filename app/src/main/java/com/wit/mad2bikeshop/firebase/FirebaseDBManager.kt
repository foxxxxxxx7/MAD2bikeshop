package com.wit.mad2bikeshop.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.model.BookStore
import timber.log.Timber

/* This is the database reference to the Firebase Realtime Database. */
var database: DatabaseReference =
    FirebaseDatabase.getInstance("https://viking-bike-hire-13ed9-default-rtdb.europe-west1.firebasedatabase.app")
        .getReference()

/**
 * We're adding a ValueEventListener to the bookings node in the database.
 *
 * @param error The error that occurred.
 */
object FirebaseDBManager : BookStore {
    override fun findAll(bookingsList: MutableLiveData<List<BookModel>>) {
        database.child("bookings")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Booking error : ${error.message}")
                }

                /**
                 * We're getting a list of all the bookings from the database, and then we're setting
                 * the value of the bookingsList MutableLiveData to the list of bookings
                 *
                 * @param snapshot DataSnapshot - This is the data that is returned from the database.
                 */
                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<BookModel>()
                    val children = snapshot.children
                    children.forEach {
                        val booking = it.getValue(BookModel::class.java)
                        localList.add(booking!!)
                    }
                    database.child("bookings")
                        .removeEventListener(this)

                    bookingsList.value = localList
                }
            })
    }

    /* This is a function that is used to find all the bookings that are associated with a user. */
    override fun findAll(userid: String, bookingsList: MutableLiveData<List<BookModel>>) {
        Timber.i(userid)
        database.child("user-bookings").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Booking error : ${error.message}")
                }

                /**
                 * We are getting the list of bookings from the database and adding them to a local
                 * list. Then we are removing the listener and setting the value of the bookingsList to
                 * the local list
                 *
                 * @param snapshot DataSnapshot - This is the data that is returned from the database.
                 */
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
     * We are getting the booking details from the database using the userid and bookingid
     *
     * @param userid The user id of the user who made the booking
     * @param bookingid The id of the booking you want to retrieve
     * @param booking MutableLiveData<BookModel>
     */
    override fun findById(userid: String, bookingid: String, booking: MutableLiveData<BookModel>) {

        database.child("user-bookings").child(userid)
            .child(bookingid).get().addOnSuccessListener {
                booking.value = it.getValue(BookModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener {
                Timber.e("firebase Error getting data $it")
            }
    }

    /**
     * We create a new booking in the database by creating a new key, adding the booking to the
     * bookings node, and adding the booking to the user-bookings node
     *
     * @param firebaseUser MutableLiveData<FirebaseUser>
     * @param booking BookModel - This is the booking object that we want to add to the database.
     * @return A MutableLiveData<BookModel>
     */
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

    /**
     * We're deleting the booking from the bookings node and the user-bookings node
     *
     * @param userid The user id of the user who made the booking.
     * @param bookingid The id of the booking to be deleted.
     */
    override fun delete(userid: String, bookingid: String) {

        val childDelete: MutableMap<String, Any?> = HashMap()
        childDelete["/bookings/$bookingid"] = null
        childDelete["/user-bookings/$userid/$bookingid"] = null

        database.updateChildren(childDelete)
    }

    /**
     * We create a map of the booking object, then we create a map of the booking object and the
     * userid, then we update the database with the two maps
     *
     * @param userid The user id of the user who made the booking.
     * @param bookingid The id of the booking to update
     * @param booking BookModel - this is the booking object that we want to update.
     */
    override fun update(userid: String, bookingid: String, booking: BookModel) {

        val bookingValue = booking.toMap()

        val childUpdate: MutableMap<String, Any?> = HashMap()
        childUpdate["bookings/$bookingid"] = bookingValue
        childUpdate["user-bookings/$userid/$bookingid"] = bookingValue

        database.updateChildren(childUpdate)
    }

    /**
     * Update the imageUri of the user in all of their bookings, and update the imageUri of all
     * bookings that match the user's bookings
     *
     * @param userid The user's id
     * @param imageUri The imageUri of the image that was just uploaded
     */
    fun updateImageRef(userid: String, imageUri: String) {

        val userBookings = database.child("user-bookings").child(userid)
        val allBookings = database.child("bookings")

        userBookings.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all bookings that match 'it'
                        val booking = it.getValue(BookModel::class.java)
                        allBookings.child(booking!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }
}