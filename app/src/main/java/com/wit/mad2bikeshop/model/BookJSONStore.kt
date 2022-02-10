package com.wit.mad2bikeshop.model

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.nio.file.Files.exists
import java.util.*




val JSON_FILE = "bookings.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<BookModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()

class BookJSONStore : BookStore{

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

    override fun create(booking: BookModel) {
        booking.id = generateRandomId()
        booking.add(bike)
        serialize()
    }


    override fun update(bike: BikeShopModel) {
        val bikesList = findAll() as ArrayList<BikeShopModel>
        var foundBike: BikeShopModel? = bikesList.find { p -> p.id == bike.id }
        if (foundBike != null) {
            foundBike.title = bike.title
            foundBike.size = bike.size
            foundBike.style = bike.style
            foundBike.gender = bike.gender
            foundBike.price = bike.price
            foundBike.condition = bike.condition
            foundBike.comment = bike.comment
            foundBike.image = bike.image
            foundBike.lat = bike.lat
            foundBike.lng = bike.lng
            foundBike.zoom = bike.zoom
        }
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(bikes, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        bikes = Gson().fromJson(jsonString, listType)
    }

    override fun delete(bike: BikeShopModel) {
        bikes.remove(bike)
        serialize()
    }
}