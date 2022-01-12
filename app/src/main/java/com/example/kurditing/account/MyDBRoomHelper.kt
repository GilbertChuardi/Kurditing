package com.example.kurditing.account

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Buat room database dengan entity berupa HistoryTransaction dan UserName menggunakan abstract class
@Database(entities = [HistoryTransaction::class, UserName::class], version = 2)
abstract class MyDBRoomHelper : RoomDatabase(){
    // fungsi abstract untuk diakses oleh instance dari room database yang akan dibuat nantinya
    abstract fun historyTransactionDAO() : HistoryTransactionDAO
    abstract fun usernameDAO() : UserNameDAO

    // companion object untuk memungkinkan inisialisasi singleton, agar room database tidak perlu
    // dibuild berulang-ulang
    companion object {
        @Volatile
        // inisialiasi instance dengan nilai null
        private var instance: MyDBRoomHelper? = null

        // fungsi getInstance() untuk mendapatkan instance dari room database
        fun getInstance(context: Context): MyDBRoomHelper {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // fungsi buildDatabase untuk melakukan build database jika belum ada instance dari
        // MyDBRoomHelper
        private fun buildDatabase(context: Context): MyDBRoomHelper {
            return Room.databaseBuilder(context, MyDBRoomHelper::class.java, "kurditing.db")
                .build()
        }
    }
}