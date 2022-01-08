package com.example.kurditing.utils

import android.content.Context
import android.content.SharedPreferences

// class Preferences yang menerima satu buah parameter context, dalam kasus ini digunakan untuk
// menyimpan data user
class Preferences(val context: Context){
    // buat companion object (variabel static) berupa konstanta USER_PREF yang akan digunakan pada
    // pembuatan sharedPreferences
    companion object{
        const val USER_PREF = "USER_PREF"
    }

    // inisialisasi nilai sharedPreferences dengan hasil return dari getSharedPreferences dengan
    // argumen USER_PREF dan mode bernilai 0 (Context.MODE_PRIVATE)
    var sharePreferences: SharedPreferences = context.getSharedPreferences(USER_PREF, 0)

    // fungsi setValues dengan parameter key dan value untuk melakukan set value pada sharedPreferences
    fun setValues(key: String, value: String){
        // inisialisasi editor sharedPreferences
        val editor:SharedPreferences.Editor = sharePreferences.edit()
        // lakukan putString terhadap pasangan key-value
        editor.putString(key, value)
        // lakukan apply terhadap penulisan data
        editor.apply()
    }

    // fungsi getValue dengan parameter key untuk mendapat value pada sharedPreferences dengan kunci
    // bernilai key
    fun getValues(key: String) : String? {
        // mereturn nilai yang tersimpan pada sharedPreferences dengan default value string kosong
        return sharePreferences.getString(key, "")
    }

    // fungsi clearValues untuk menghapus semua data yang tersimpan pada sharedPreferences
    fun clearValues() {
        // lakukan clear kemudian apply pada editor sharedPreferences
        sharePreferences.edit().clear().apply()
    }
}