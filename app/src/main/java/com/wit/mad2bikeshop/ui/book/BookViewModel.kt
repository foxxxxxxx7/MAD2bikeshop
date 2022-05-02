package com.wit.mad2bikeshop.ui.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.wit.mad2bikeshop.firebase.FirebaseDBManager
import com.wit.mad2bikeshop.model.BookManager
import com.wit.mad2bikeshop.model.BookModel

class BookViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addBook(firebaseUser: MutableLiveData<FirebaseUser>,
                booking: BookModel) {
        status.value = try {
            //DonationManager.create(donation)
            FirebaseDBManager.create(firebaseUser,booking)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun updateBook(booking: BookModel){
        status.value = try {
        BookManager.update(booking)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}