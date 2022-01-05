package com.example.kurditing

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.kurditing.model.Course

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

        // inisialisasi intent
        val i = Intent(context, DescriptionActivity::class.java)
        // memasukkan data kedalam intent
        i.putExtra("data", mockData())
        // membersihkan stack memory
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)


        // membuild notifikasi
        val mBuilder = NotificationCompat.Builder(context!!, Channel_id)
                .setSmallIcon(R.drawable.ic_kurditing) // menset icon pada notifkiasi
                .setContentText(intent?.getStringExtra(EXTRA_PESAN) ?: "Tidak ada pesan") // menset pesan
                .setContentTitle("Kurditing") // menset judul
                .setContentIntent(pendingIntent)
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
        mNotificationManager.notify(NotifyId, mBuilder.build())
    }

    // fungsi untuk memasukkan data
    private fun mockData(): Course {
        // inisialisasi Course
        val data = Course()
        // masukkan desc
        data.desc = "Saat ini iklan video semakin beragam saja, baik iklan di televisi ataupun iklan di media sosial. Iklan-iklan tersebut dikemas dengan sangat menarik. Tujuaannya adalah agar bisa berkesar di mata audience yang melihatnya. Inilah tips-tips yang bisa diterapkan para pebisnis agar iklan video yang disajikan, mampu mengambil hati masyarakat karena menarik."
        // masukkan judul
        data.judul = "Cara Membuat Ads Video untuk UMKM"
        // masukkan owner
        data.owner = "David Sedaris"
        // masukkan poster
        data.poster = "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Fimage%203.png?alt=media&token=8c21337d-e4f7-427b-b111-34030ddc9675"
        // masukkan harga
        data.harga = "125000"
        // masukkan rating
        data.rating = "4.5"
        // masukkan owner poster
        data.owner_poster = "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/images%2Frk%201.png?alt=media&token=e661920d-c15a-4723-98e7-0f3a24d1b0d2"
        // mengembalikan data
        return data
    }
}