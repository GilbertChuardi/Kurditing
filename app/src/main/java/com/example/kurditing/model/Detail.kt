package com.example.kurditing.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Detail (
    var desc:String ? = "",
    var judul:String ? = "",
    var poster:String ? = "",
    var harga:String ? = "",
    var rating:String ? = ""
) : Parcelable
