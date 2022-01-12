package com.example.kurditing.home

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kurditing.R
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    // pada HomeActivity deklarasikan notificationManager dan inisialisasikan dengan nilai null
    var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bnv = bnv
        val navController = findNavController(R.id.fragment)

        bnv.setupWithNavController(navController)

        var cek = intent.getStringExtra("course")
        if(cek == "course"){
            navController.navigate(R.id.courseFragment)
        }

        // lakukan assignment pada notificationManager dengan notification system service
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // panggil fungsi untuk membuat notification channel
        createNotificationChannels()
    }

//    private fun createNotificationGroup() {
//        // memastikan android adalah android Oreo dan mendaftarkan
//        // 2 grup berbeda untuk notification
//        // jika ada grup berbeda, anda dapat menambahkan list
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val list = mutableListOf<NotificationChannelGroup>()
//            list.add(NotificationChannelGroup(radioButton1.getText().toString(),
//                    radioButton1.getText()))
//            list.add(NotificationChannelGroup(radioButton2.getText().toString(),
//                    radioButton2.getText()))
//            notificationManager!!.createNotificationChannelGroups(list)
//        }
//    }

//     fun showNotification() {
//        val notification = NotificationCompat.Builder(this, "audio")
//            .setContentTitle("test")
//            .setContentText("Nice")
//            .setSmallIcon(R.drawable.ic_kurditing)
//            .addAction(R.drawable.ic_pause, "Pause", null)
//            .build()
//        notificationManager!!.notify(1, notification)
//    }

    // fungsi untuk membuat notification channel
    private fun createNotificationChannels() {
        // lakukan pengecekan agar hanya dijalankan ketika versi SDK_INT hanya berjalan pada Android 8.0 (API level 26)
        // atau di atasnya
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // buat suara untuk notification menggunakan class RingtoneManager
            val notificationSound = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            // variable att untuk menampung AudioAttributes untuk menandakan bahwa diterapkan pada notification
            val att = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()

            // inisialisasi channel notification untuk audio
            val audioChannel = NotificationChannel(
                "audio",
                "Audio Playback",
                NotificationManager.IMPORTANCE_HIGH
            )
            // pengisian nilai atribut description, vibrationPattern (getaran), setSound, lightColor
            // serta enable Light dan juga Vibration
            audioChannel.description = "Notification Channel for Audio Playback"
            audioChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
            audioChannel.setSound(notificationSound, att)
            audioChannel.lightColor = Color.RED
            audioChannel.enableLights(true)
            audioChannel.enableVibration(true)
            // untuk enable dot (badge) pada notification
            audioChannel.setShowBadge(true)

            // inisialisasi channel notification untuk video
            val videoChannel = NotificationChannel(
                "video",
                "Video Playback",
                NotificationManager.IMPORTANCE_HIGH
            )
            // pengisian nilai atribut description, vibrationPattern (getaran), setSound, lightColor
            // serta enable Light dan juga Vibration
            videoChannel.description = "Notification Channel for Video Playback"
            videoChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
            videoChannel.setSound(notificationSound, att)
            videoChannel.lightColor = Color.RED
            videoChannel.enableLights(true)
            videoChannel.enableVibration(true)

            // buat dan daftarkan audioChannel beserta videoChannel menggunakan notificationManager
            notificationManager?.createNotificationChannel(audioChannel)
            notificationManager?.createNotificationChannel(videoChannel)
        }
    }
}