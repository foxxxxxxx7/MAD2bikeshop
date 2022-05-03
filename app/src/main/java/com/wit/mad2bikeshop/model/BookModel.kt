package com.wit.mad2bikeshop.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class BookModel(
    var id : Long = 0,
    var uid: String? = "",
    var name: String = "N/A",
    var phoneNumber: String = "N/A",
    var email: String? = "N/A",
    var date: String = "",
    var bike: Int = 0,
    /*var lat: Double = 0.0,
    var longitude: Double = 0.0,
    var zoom: Float = 0f,*/
    var pickup: String = "",
    var dropoff: String = "",
    var price: Double = 20.0,
    /*var amount: Int = 0*/
) : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "uid" to uid,
            "name" to name,
            "phoneNumber" to phoneNumber,
            "email" to email,
            "date" to date,
            "bike" to bike,
            "pickup" to pickup,
            "dropoff" to dropoff,
            "price" to price
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