package com.wit.mad2bikeshop.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface BookStore {
        fun findAll(bookingsList:
                    MutableLiveData<List<BookModel>>
        )
        fun findAll(userid:String,
                    bookingsList:
                    MutableLiveData<List<BookModel>>)
        fun findById(userid:String, bookingid: String,
                     booking: MutableLiveData<BookModel>)
        fun create(firebaseUser: MutableLiveData<FirebaseUser>, booking: BookModel)
        fun delete(userid:String, bookingid: String)
        fun update(userid:String, bookingid: String, booking: BookModel)
    }
