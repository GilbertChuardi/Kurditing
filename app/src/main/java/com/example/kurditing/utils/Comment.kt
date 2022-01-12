package com.example.kurditing.utils

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment (
        var nama:String ? = "",
        var comment: String ? = "",
        var profile:String ? = "",
) : Parcelable