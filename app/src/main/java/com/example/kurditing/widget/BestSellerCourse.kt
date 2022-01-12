package com.example.kurditing.widget

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// data class BestSellerCourse guna menampung data kursus best seller yang akan ditampilkan
// pada widget yang disertai anotasi Parcelize dan mengimplementasi Parcelable
@Parcelize
data class BestSellerCourse(
    var title:String ? = "",
    var author:String ? = "",
) : Parcelable
