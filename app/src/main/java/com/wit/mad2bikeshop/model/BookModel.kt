package com.wit.mad2bikeshop.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import java.util.*

/* This is a data class that is used to store the data of the bookings. */
@IgnoreExtraProperties
@Parcelize
data class BookModel(
//    var id : Long = 0,
    var uid: String? = "",
    var name: String = "N/A",
    var phoneNumber: String = "N/A",
    var email: String? = "N/A",
    var date: String = "",
    var bike: Int = 0,
    var profilepic: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
//    var zoom: Float = 0f,
    var pickup: String = "",
    var dropoff: String = "",
    var price: Double = 20.0,
    /*var amount: Int = 0*/
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
//            "id" to id,
            "uid" to uid,
            "name" to name,
            "phoneNumber" to phoneNumber,
            "email" to email,
            "date" to date,
            "bike" to bike,
            "profilepic" to profilepic,
            "pickup" to pickup,
            "dropoff" to dropoff,
            "price" to price,
            "latitude" to latitude,
            "longitude" to longitude
        )
    }
}

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var longitude: Double = 0.0,
    var zoom: Float = 0f,
    var depot: String = "Waterford"
) : Parcelable