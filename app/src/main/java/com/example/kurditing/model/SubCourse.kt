package com.example.kurditing.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SubCourse (
    var desc:String ? = "",
    var judul:String ? = "",
    var poster:String ? = ""
) : Parcelable
