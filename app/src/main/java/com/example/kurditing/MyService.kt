package com.example.kurditing

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.JobIntentService

// inisialisai konstanta
const val ACTION_STOP = "stop"
const val EXTRA_FINISH = "finish"

class MyService : Service() {
    //inisialisasi variabel mediaplayer
    private lateinit var mediaPlayer:MediaPlayer

    // untuk menyediakan binding terhadap service
    override fun onBind(intent: Intent): IBinder ?= null

    // fungsi yang dijalankan saat service dijalankan
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //inisialisasi link audio
        val audioPath = "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/audio%2FFading%20Lights%20Animasi%20Hitung%20Mundur%2010%20Detik.mp3?alt=media&token=a5ec65de-e5c6-4452-bea5-052363250a6a"

        // mengubah link audio menjadi uri
        val uri: Uri = Uri.parse(audioPath)

        // membuat sebuah media player untuk memutar audio pada link
        mediaPlayer = MediaPlayer.create(this, uri)

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
}