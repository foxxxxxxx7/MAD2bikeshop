package com.wit.mad2bikeshop.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wit.mad2bikeshop.model.BookManager
import com.wit.mad2bikeshop.model.BookModel

class BookingDetailViewModel : ViewModel() {
    private val booking = MutableLiveData<BookModel>()

    val observableBooking: LiveData<BookModel>
        get() = booking

    fun getBooking(id: Long) {
        booking.value = BookManager.findById(id)
    }
}