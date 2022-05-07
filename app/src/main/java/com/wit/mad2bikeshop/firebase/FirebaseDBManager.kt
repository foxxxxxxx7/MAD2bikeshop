package com.wit.mad2bikeshop.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.model.BookStore
import timber.log.Timber

var database: DatabaseReference = FirebaseDatabase.getInstance.("https://viking-bike-hire-13ed9-default-rtdb.europe-west1.firebasedatabase.app").getReference()

object FirebaseDBManager : BookStore {
    override fun findAll(bookingsList: MutableLiveData<List<BookModel>>) {
        database.child("bookings")
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
                    database.child("bookings")
                        .removeEventListener(this)

                    bookingsList.value = localList
                }
            })
    }

    override fun findAll(userid: String, bookingsList: MutableLiveData<List<BookModel>>) {
        Timber.i(userid)
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

    override fun findById(userid: String, bookingid: String, booking: MutableLiveData<BookModel>) {

        database.child("user-bookings").child(userid)
            .child(bookingid).get().addOnSuccessListener {
                booking.value = it.getValue(BookModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
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

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/bookings/$bookingid"] = null
        childDelete["/user-bookings/$userid/$bookingid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, bookingid: String, booking: BookModel) {

        val bookingValue = booking.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["bookings/$bookingid"] = bookingValue
        childUpdate["user-bookings/$userid/$bookingid"] = bookingValue

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

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