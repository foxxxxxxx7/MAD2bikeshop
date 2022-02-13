package com.wit.mad2bikeshop.model

import android.content.Context
import android.system.Os.read
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.Streams.write
import com.google.gson.reflect.TypeToken
import java.nio.file.Files.exists
import java.util.*
import com.google.gson.*
import com.wit.mad2bikeshop.exists
import com.wit.mad2bikeshop.read
import com.wit.mad2bikeshop.write


val JSON_FILE = "bookings.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<BookModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class BookJSONStore : BookStore {

    val context: Context
    var bookings = mutableListOf<BookModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<BookModel> {
        return bookings
    }

    override fun findById(id: Long): BookModel? {
        TODO("Not yet implemented")
    }

    override fun create(booking: BookModel) {
        booking.id = generateRandomId()
        bookings.add(booking)
        serialize()
    }


    override fun update(booking: BookModel) {
        val bookingsList = findAll() as ArrayList<BookModel>
        var foundBook: BookModel? = bookings.find { p -> p.id == booking.id }
        if (foundBook != null) {
            foundBook.date = booking.date
            foundBook.name = booking.name
            foundBook.phoneNumber = booking.phoneNumber
            foundBook.email = booking.email
            foundBook.pickup = booking.pickup
            foundBook.dropoff = booking.dropoff
            foundBook.price = booking.price

/*foundBook.condition = booking.condition
            foundBook.comment = booking.comment
            foundBook.image = booking.image
            foundBook.lat = booking.lat
            foundBook.lng = booking.lng
            foundBook.zoom = booking.zoom**/

        }
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(bookings, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        bookings = Gson().fromJson(jsonString, listType)
    }

    override fun delete(booking: BookModel) {
        bookings.remove(booking)
        serialize()
    }
}

