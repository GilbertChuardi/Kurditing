package com.example.kurditing.account

import androidx.room.Database
import androidx.room.RoomDatabase

// Mendeklarasikan entitas/table yang akan dibentuk pada database
@Database(entities = [HistoryTransaction::class, UserName::class], version = 2)
// Mengimplementasikan API RoomDatabase()  dengan menetukan DAO yang akan digunakan dalam class
abstract class MyDBRoomHelper : RoomDatabase(){
    // Mendeklarasikan DAO untuk HistoryTransactionDAO
    abstract fun historyTransactionDAO() : HistoryTransactionDAO
    abstract fun usernameDAO() : UserNameDAO
}