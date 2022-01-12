package com.example.kurditing.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// class data Comment dengan anotasi Parcelize yang mengandung 4 buah field, yaitu id, username,
// comment, dan profile
@Parcelize
data class Comment (
        var id : Int = 0,
        var username : String = "",
        var comment : String = "",
        var profile : String = "",
) : Parcelable {}