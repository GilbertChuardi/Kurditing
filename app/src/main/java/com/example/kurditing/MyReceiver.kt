package com.example.kurditing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

// class MyReceiver untuk menangkap broadcast dari stopIntent yang dibuat pada DescriptionActivity
class MyReceiver: BroadcastReceiver() {
    // fungsi onReceiver dijalankan ketika menerima broadcast
    override fun onReceive(context: Context, intent: Intent) {
        // lakukan pengecekan apakah nilai atribut action dari intent adalah "STOP"
        if (intent.action == "STOP") {
            // jika ya, maka panggil method stop() dari MyService
            MyService.stop()
        }
    }
}