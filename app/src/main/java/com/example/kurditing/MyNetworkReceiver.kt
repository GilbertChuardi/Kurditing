package com.example.kurditing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

// buat sebuah class MyNetworkReceiver seagai turunan dari BroadcastReceiver
class MyNetworkReceiver : BroadcastReceiver() {
    // pada method onReceive lakukan pengecekan terhadap networkReceiverListener
    override fun onReceive(context: Context, intent: Intent) {
        // apabila tidak null (sudah diassign pada HomeActivity) , maka panggil method onNetworkConnectionChanged dengan parameter
        // isConnectedOrConnecting, dimana fungsi dari method isConnectedOrConnecting untuk mengecek
        // apakah sedang dalam keadaan terkoneksi atau sedang berusaha melakukan koneksi internet
        if (networkReceiverListener != null) {
            networkReceiverListener!!.onNetworkConnectionChanged(isConnectedOrConnecting(context))
        }
    }

    // method isConnectedOrConnecting yang mereturn nilai boolean
    private fun isConnectedOrConnecting(context: Context): Boolean {
        // simpan nilai ConnectivityManager ke dalam sebuah variabel
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // kemudian akses activeNetworkInfo untuk mendapatkan detail dari informasi jaringan internet
        val networkInfo = connMgr.activeNetworkInfo
        // return nilai true ketika networkInfo memiliki nilai dan sedang dalam keadaan terkoneksi atau
        // berusaha melakukan koneksi
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    // sebuah interface yang akan diimplement oleh HomeActivity agar dapat dioverride dalam HomeActivity dan
    // dapat diimplementasikan logicnya
    interface NetworkReceiverListener {
        // di dalamnya terdapat sebuah method yang akan diimplemen dalam HomeActivity
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    // buat sebuah companion object yang di dalamnya terdapat networkReceiverListener yang diinisialisasikan dengan nilai null
    companion object {
        var networkReceiverListener: NetworkReceiverListener? = null
    }
}