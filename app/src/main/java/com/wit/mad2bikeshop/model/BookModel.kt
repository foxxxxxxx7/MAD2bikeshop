package com.wit.mad2bikeshop.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookModel(var id: Long = 0,
                     val name: String = "N/A",
                     val phoneNumber: String = "N/A",
                     val email: String = "N/A",
                    /* val pickup: Location= ??*/
                    /* val dropoff: Location= ??*/
                    /* val bikes: ArrayOfBikes = ??*/
                     /*val pickup: Location =*/
                     val lat: Double = 0.0,
                     val longitude: Double = 0.0,
                     var zoom: Float = 0f,
                     val pickup : String = "Waterford",
                     val dropoff : String = "Dungarvan",
                     val amount: Int = 0) : Parcelable

@Parcelize
data class Location(val lat: Double = 0.0,
                    val longitude: Double = 0.0,
                    var zoom: Float = 0f,
                    val depot : String = "Waterford") : Parcelable