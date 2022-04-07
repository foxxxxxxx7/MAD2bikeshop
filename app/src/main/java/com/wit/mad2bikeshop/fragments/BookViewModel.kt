package com.wit.mad2bikeshop.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wit.mad2bikeshop.model.BookManager
import com.wit.mad2bikeshop.model.BookModel

class BookViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addBook(booking: BookModel) {
        status.value = try {
            BookManager.create(booking)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}