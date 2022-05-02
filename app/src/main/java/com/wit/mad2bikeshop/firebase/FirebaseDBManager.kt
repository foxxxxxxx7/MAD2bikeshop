package com.wit.mad2bikeshop.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.model.BookStore

object FirebaseDBManager : BookStore {
    override fun findAll(bookingsList: MutableLiveData<List<BookModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, bookingsList: MutableLiveData<List<BookModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(userid: String, bookingid: String, booking: MutableLiveData<BookModel>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, booking: BookModel) {
        TODO("Not yet implemented")
    }

    override fun delete(userid: String, bookingid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, bookingid: String, booking: BookModel) {
        TODO("Not yet implemented")
    }
}