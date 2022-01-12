package com.example.kurditing.account

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// membuat struktur tabel pada database
@Entity
data class HistoryTransaction(@PrimaryKey var _id : Int,
                              @ColumnInfo(name = "course_name") var course_name : String  = "",
                              @ColumnInfo(name = "price") var price : Int = 0 ,
                              @ColumnInfo(name = "created_at") var created_at : String = "") {
}