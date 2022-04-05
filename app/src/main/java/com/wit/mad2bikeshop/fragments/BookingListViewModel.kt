package com.wit.mad2bikeshop.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wit.mad2bikeshop.model.BookManager
import com.wit.mad2bikeshop.model.BookModel

class BookingListViewModel : ViewModel() {

    private val bookingList = MutableLiveData<List<BookModel>>()

    val observableBookingList: LiveData<List<BookModel>>
        get() = bookingList

    init {
        load()
    }

    fun load() {
        bookingList.value = BookManager.findAll()
    }
}