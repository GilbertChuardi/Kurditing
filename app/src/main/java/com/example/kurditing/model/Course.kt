package com.example.kurditing.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// data class Course guna menampung data kursus
@Parcelize
data class Course (
    var desc:String ? = "",
    var judul:String ? = "",
    var owner:String ? = "",
    var poster:String ? = "",
    var harga:String ? = "",
    var rating:String ? = "",
    var owner_poster:String ? = ""
) : Parcelable
