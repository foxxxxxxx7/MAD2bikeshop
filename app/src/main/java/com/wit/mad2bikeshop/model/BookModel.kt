package com.wit.mad2bikeshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookModel(var id: Long = 0,
                     var name: String = "N/A",
                     var phoneNumber: String = "N/A",
                     var email: String = "N/A",
                     /*var lat: Double = 0.0,
                     var longitude: Double = 0.0,
                     var zoom: Float = 0f,*/
                     var pickup : String = "Waterford",
                     var dropoff : String = "Dungarvan",
                     var price: Int = 0,
                     /*var amount: Int = 0*/) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var longitude: Double = 0.0,
                    var zoom: Float = 0f,
                    var depot : String = "Waterford") : Parcelable