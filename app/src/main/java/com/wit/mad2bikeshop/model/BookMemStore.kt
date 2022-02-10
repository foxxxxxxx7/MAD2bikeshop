package com.wit.mad2bikeshop.model

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class BookMemStore : BookStore {

    val bookings = ArrayList<BookModel>()

    override fun findAll(): List<BookModel> {
        return bookings
    }

    override fun findById(id:Long) : BookModel? {
        val foundBook: BookModel? = bookings.find { it.id == id }
        return foundBook
    }

    override fun create(booking: BookModel) {
        booking.id = getId()
        bookings.add(booking)
        logAll()
    }

    fun logAll() {
        Timber.v("** Bookings List **")
        bookings.forEach { Timber.v("Book ${it}") }
    }
}