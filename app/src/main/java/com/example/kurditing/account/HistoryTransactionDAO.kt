package com.example.kurditing.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryTransactionDAO {
    @Query("SELECT * FROM HistoryTransaction")
    fun getAllData() : List<HistoryTransaction>

    @Insert
    fun insertAll(vararg historyTransacntion : HistoryTransaction)

    @Query("DELETE FROM HistoryTransaction WHERE _id = :id")
    fun deleteAll(id : Int)

}