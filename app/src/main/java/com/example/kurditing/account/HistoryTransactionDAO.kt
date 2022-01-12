package com.example.kurditing.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// Membuat query yang diperlukan
@Dao
interface HistoryTransactionDAO {
    // Query untuk mengambil semua data dari table HistoryTransaction
    @Query("SELECT * FROM HistoryTransaction")
    fun getAllData() : List<HistoryTransaction>

    // Query untuk menambahkan data kedalam tabel HistoryTransaction
    @Insert
    fun insertAll(vararg historyTransacntion : HistoryTransaction)

}