//package com.wit.mad2bikeshop.model
//
//import android.content.Context
//import android.system.Os.read
//import com.google.gson.Gson
//import com.google.gson.GsonBuilder
//import com.google.gson.internal.Streams.write
//import com.google.gson.reflect.TypeToken
//import java.nio.file.Files.exists
//import java.util.*
//import com.google.gson.*
//import com.wit.mad2bikeshop.exists
//import com.wit.mad2bikeshop.read
//import com.wit.mad2bikeshop.write
//
//
// This is a constant variable that is used to store the file name of the JSON file.
//
//val JSON_FILE = "bookings.json"
//val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
//val listType = object : TypeToken<ArrayList<BookModel>>() {}.type
//
//*
// * This function generates a random id.
// *
// * @return A random long value
//
//
//fun generateRandomId(): Long {
//    return Random().nextLong()
//}
//
// This is the BookJSONStore class. It is a class that implements the BookStore interface. It has a
//constructor that takes in a context and a JSON_FILE. It also has a mutable list of BookModel
//objects.
//
//class BookJSONStore : BookStore {
//
//    val context: Context
//    var bookings = mutableListOf<BookModel>()
//
//    constructor (context: Context) {
//        this.context = context
//        if (exists(context, JSON_FILE)) {
//            deserialize()
//        }
//    }
//
//*
//     * It returns a list of bookings.
//     *
//     * @return A list of BookingModel objects
//
//
//    override fun findAll(): MutableList<BookModel> {
//        return bookings
//    }
//
//*
//     * It returns a BookModel object.
//     *
//     * @param id Long - The id of the book to find
//
//
//    override fun findById(id: Long): BookModel? {
//        TODO("Not yet implemented")
//    }
//
//*
//     * It creates a new booking.
//     *
//     * @param booking BookModel - this is the parameter that is passed in from the BookController.
//
//
//    override fun create(booking: BookModel) {
//        booking.id = generateRandomId()
//        bookings.add(booking)
//        serialize()
//    }
//
//
//*
//     * The function takes a BookModel object as a parameter, finds the object in the list of BookModel
//     * objects, and updates the object with the new values
//     *
//     * @param booking BookModel - this is the booking that we are updating
//
//
//    override fun update(booking: BookModel) {
//        val bookingsList = findAll() as ArrayList<BookModel>
//        var foundBook: BookModel? = bookings.find { p -> p.id == booking.id }
//        if (foundBook != null) {
//            foundBook.date = booking.date
//            foundBook.name = booking.name
//            foundBook.phoneNumber = booking.phoneNumber
//            foundBook.email = booking.email
//            foundBook.pickup = booking.pickup
//            foundBook.dropoff = booking.dropoff
//            foundBook.price = booking.price
//
//foundBook.condition = booking.condition
//            foundBook.comment = booking.comment
//            foundBook.image = booking.image
//            foundBook.lat = booking.lat
//            foundBook.lng = booking.lng
//            foundBook.zoom = booking.zoom*
//
//
//        }
//        serialize()
//    }
//
//
//*
//     * It serializes the data in the bookings list to a JSON file.
//
//
//    private fun serialize() {
//        val jsonString = gsonBuilder.toJson(bookings, listType)
//        write(context, JSON_FILE, jsonString)
//    }
//
//*
//     * It reads the JSON file and converts it to a list of Booking objects.
//
//
//    private fun deserialize() {
//        val jsonString = read(context, JSON_FILE)
//        bookings = Gson().fromJson(jsonString, listType)
//    }
//
//*
//     * It removes the booking from the list of bookings and then serializes the list.
//     *
//     * @param booking BookModel - The booking to be deleted
//
//
//    override fun delete(booking: BookModel) {
//        bookings.remove(booking)
//        serialize()
//    }
//}
//
