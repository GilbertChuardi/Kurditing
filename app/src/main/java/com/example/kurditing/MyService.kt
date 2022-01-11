package com.example.kurditing

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_description.*

// inisialisai konstanta
const val ACTION_STOP = "stop"
const val EXTRA_FINISH = "finish"

class MyService : Service() {
    // untuk menyediakan binding terhadap service
    override fun onBind(intent: Intent): IBinder ?= null

    // fungsi yang dijalankan saat service dijalankan
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // lakukan assignment pada notificationManager dengan notification system service
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //inisialisasi audio
        val audio = R.raw.audio

        // membuat sebuah media player untuk memutar audio
        mediaPlayer = MediaPlayer.create(this, audio)

        // menjalankan fungsi ketika audio selesai
        mediaPlayer.setOnCompletionListener {
            // inisialisasi intent
            var intent = Intent(ACTION_STOP)
            // memasukkan extra pada intent
            intent.putExtra(EXTRA_FINISH, true)
            // melakukan pengiriman broadcast dengan argumen intent yang telah dibuat
            sendBroadcast(intent)
        }

        // menjalankan media player dan memutar audio
        mediaPlayer.start()
        // untuk membuat dan menjalankan kembali service setelah fungsi onStartCommand dijalankan dan service sudah dimatikan (kill).

        return START_STICKY
    }

    // fungsi yang dijalankan ketika service didestroy
    override fun onDestroy() {
        // panggil method ondestroy() dari class parent
        super.onDestroy()
        // menhentikan media player dan audio
        mediaPlayer.stop()
    }

    // companion object berfungsi untuk membuat properti static pada kotlin
    companion object {
        // deklarasi mediaPlayer serta notificationManager
        lateinit var mediaPlayer: MediaPlayer
        var notificationManager: NotificationManager? = null

        // method stop() untuk menghentikan mediaPlayer serta menghapus notification dengan id 2
        fun stop() {
            mediaPlayer.stop()
            notificationManager?.cancel(2)
        }
    }
}