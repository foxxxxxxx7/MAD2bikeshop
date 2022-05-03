package com.wit.mad2bikeshop.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.model.BookStore
import timber.log.Timber

var database: DatabaseReference = FirebaseDatabase.getInstance("https://viking-bike-hire-13ed9-default-rtdb.europe-west1.firebasedatabase.app").getReference()

object FirebaseDBManager : BookStore {
    override fun findAll(bookingsList: MutableLiveData<List<BookModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, bookingsList: MutableLiveData<List<BookModel>>) {
        Timber.i("Booking List 2")
        Timber.i(userid)
        database.child("user-bookings").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Booking error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    Timber.i("Booking List 3")
                    val localList = ArrayList<BookModel>()
                    val children = snapshot.children
                    children.forEach {
                        val booking = it.getValue(BookModel::class.java)
                        localList.add(booking!!)
                    }
                    database.child("user-bookings").child(userid)
                        .removeEventListener(this)
                    Timber.i("Booking List 4")
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
        TODO("Not yet implemented")
    }

    override fun update(userid: String, bookingid: String, booking: BookModel) {
        TODO("Not yet implemented")
    }
}