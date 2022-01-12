package com.example.kurditing.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// class UserName untuk pembuatan tabel / entitas
@Entity
data class UserName(@PrimaryKey var _id : Int,
                    @ColumnInfo(name = "name") var name : String  = "") {

}