package com.example.kurditing.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// menambahkan anotasi Parcelize untuk memudahkan membuat Parcelable pada Kotlin
@Parcelize
//data class Course (kursus) yang berisi atribut-atribut kursus
data class Course (
    var desc:String ? = "",
    var judul:String ? = "",
    var owner:String ? = "",
    var poster:String ? = "",
    var harga:String ? = "",
    var rating:String ? = "",
    var owner_poster:String ? = ""
) : Parcelable
// menunjukkan bahwa class menggunakan interface parcelable
