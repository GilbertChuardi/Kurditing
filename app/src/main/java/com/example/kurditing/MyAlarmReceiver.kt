package com.example.kurditing

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

// inisialisasi konstanta
const val EXTRA_PESAN = "EXTRA_PESAN"

class MyAlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        // inisialisasi notify id
        val NotifyId = 30103
        // inisialisasi id channel
        val Channel_id = "my_channel_01"
        // inisialisasi nama notifikasi
        val name = "ON/OFF"
        // inisialisasi tingkap kepentingan notifikasi
        val importance = NotificationManager.IMPORTANCE_HIGH
        // membuat noficitaion channel
        val nNotifyChannel = NotificationChannel(Channel_id, name, importance)

        // membuild notifikasi
        val mBuilder = NotificationCompat.Builder(context!!,Channel_id)
                .setSmallIcon(R.drawable.ic_kurditing) // menset icon pada notifkiasi
                .setContentText(intent?.getStringExtra(EXTRA_PESAN) ?: "Tidak ada pesan") // menset pesan
                .setContentTitle("Kurditing") // menset judul
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // menset prioritas notifkasi
        // inisialisasi notification manager
        var mNotificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // membersihkan notification manager
        for(s in mNotificationManager.notificationChannels){
            mNotificationManager.deleteNotificationChannel(s.id)
        }
        // membuat notification channel
        mNotificationManager.createNotificationChannel(nNotifyChannel)
        // menampilkan notifikasi
        mNotificationManager.notify(NotifyId,mBuilder.build())
    }
}