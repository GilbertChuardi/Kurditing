package com.example.kurditing

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment (
    var id : Int = 0,
    var comment : String ="",
    var username : String= "",
) : Parcelable{}