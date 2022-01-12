package com.wiga.app2

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Comment (
    var id : Int = 0,
    var comment : String ="",
    var username : String= "",
) : Parcelable {}