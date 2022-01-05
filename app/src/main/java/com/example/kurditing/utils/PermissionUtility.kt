package com.example.kurditing.utils

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtility {
    // fungsi untuk cek permission
    fun checkAndRequestPermission(activity: Activity, listPermission: MutableList<String>): Boolean {
        // inisialisasi list permission
        val listPermissionToRequest: MutableList<String> = mutableListOf()

        // pengulangan untuk permission
        for (permission in listPermission) {
            // pengecekan apakah izin diberikan atau tidak
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                // menambahkan permission kedalam list
                listPermissionToRequest.add(permission)
            }
        }

        // pengecekan jika request permission kosong atau tidak
        if (listPermissionToRequest.isNotEmpty()) {
            // menjalankan fungsi requestPermission
            requestPermission(activity, listPermissionToRequest)
            // mengembalikan false
            return false
        }
        // mengambilkan true
        return true
    }

    // fungsi requestPermission untuk pengecekan permission
    private fun requestPermission(activity: Activity, listOfPermission: MutableList<String>) {
        // inisialisasi listpermission
        val listPermission = listOfPermission.toTypedArray()
        // inisialisasi status permission
        val requestPermission = 1
        // menjalankan fungsi requestPermission
        ActivityCompat.requestPermissions(activity, listPermission, requestPermission)
    }
}