package com.example.kurditing.account

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// membuat DAO (Data Access Object) untuk mengakses data dari room database
@Dao
interface UserNameDAO {
    // query untuk mendapatkan list username
    @Query("SELECT * FROM UserName")
    fun getAllData() : List<UserName>

    // insert untuk memasukkan username
    @Insert
    fun insertAll(vararg userName : UserName)

    // query untuk melakukan update dengan id tertentu
    @Query("UPDATE UserName SET name = :name WHERE _id = :id")
    fun updateData(name : String, id : Int)
}