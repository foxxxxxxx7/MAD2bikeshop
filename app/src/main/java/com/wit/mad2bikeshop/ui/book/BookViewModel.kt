package com.wit.mad2bikeshop.ui.book

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.wit.mad2bikeshop.firebase.FirebaseDBManager
import com.wit.mad2bikeshop.firebase.FirebaseImageManager
import com.wit.mad2bikeshop.model.BookManager
import com.wit.mad2bikeshop.model.BookModel

/* This is the ViewModel class for the Booking page. It is used to store and manage UI-related data in
a lifecycle conscious way. This allows data to survive configuration changes such as screen
rotations. */
class BookViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    /**
     * This function takes in a FirebaseUser and a BookModel and adds the BookModel to the Firebase
     * database
     *
     * @param firebaseUser The user who is logged in.
     * @param booking BookModel - This is the model class that we created earlier.
     */
    fun addBook(
        firebaseUser: MutableLiveData<FirebaseUser>,
        booking: BookModel
    ) {
        status.value = try {
            booking.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser, booking)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

//    fun updateBook(booking: BookModel){
//        status.value = try {
//        BookManager.update(booking)
//            true
//        } catch (e: IllegalArgumentException) {
//            false
//        }
//    }
}