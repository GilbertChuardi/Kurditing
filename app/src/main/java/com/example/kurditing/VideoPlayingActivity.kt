package com.example.kurditing

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.MediaController
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kurditing.model.Course
import kotlinx.android.synthetic.main.activity_video_playing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class VideoPlayingActivity : AppCompatActivity() {
    // pada VidePlayingActivity deklarasikan notificationManager dan inisialisasikan dengan nilai null
    var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_playing)

        // terima data kursus dari extra yang dikirim melalui intent
        val data = intent.getParcelableExtra<Course>("data")

        // lakukan assignment pada notificationManager dengan notification system service
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // panggil showNotification dengan argumen url dari gambar kursus ketika activity dijalankan
        showNotification(data?.poster)

        // inisialisasi path dari video, ubah menjadi uri, kemudian set ke dalam video_view
        val videoPath = "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/videos%2FRecord%20of%20Ragnarok%20-%20Teaser%20Trailer%20(ENG%20subbed).mp4?alt=media&token=20389dec-36d4-438a-b687-2da9ff44c1b5"
        val uri: Uri = Uri.parse(videoPath)
        video_view.setVideoURI(uri)
        // inisialisasi mediaController kemudian set pada video_view, beserta set anchor view dari mediaController
        // menjadi video_view
        val mediaController = MediaController(this)
        video_view.setMediaController(mediaController)
        mediaController.setAnchorView(video_view)
        // play video
        video_view.start()
    }

    // fungsi showNotification dengan sebuah parameter url untuk menampilkan notification
    fun showNotification(url: String?) {
        // inisialisasi collapsedView dengan objek RemoteViews, passing packageName (com.example.kurditing)
        // serta file layout notification_collapsed.xml yang telah dibuat
        val collapsedView = RemoteViews(
            packageName,
            R.layout.notification_collapsed
        )
        // inisialisasi expandedView dengan objek RemoteViews, passing packageName (com.example.kurditing)
        // serta file layout notification_expanded.xml yang telah dibuat
        val expandedView = RemoteViews(
            packageName,
            R.layout.notification_expanded
        )
        // panggil fungsi appllyImageUrl dengan passing argumen url dan RemoteViews expandedView
        applyImageUrl(url, expandedView)

        // buat notification menggunakan objek Builder, kemudian setSmallIcon dengan logo kurditing
        // disertai set custom layout untuk state collapsed dan state expanded
        val notification = NotificationCompat.Builder(this, "video")
            .setSmallIcon(R.drawable.ic_kurditing)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
        // setOngoing menjadi nilai true agar notification tidak dapat dapat dihilangkan selagi video
        // sedang dijalankan
        notification.setOngoing(true)

        // panggil method notify dari notificationManager untuk melakukan notifikasi, serta dimunculkan
        // pada status bar
        notificationManager!!.notify(1, notification.build())
    }

    // fungsi applyImageUrl untuk mengkonversi url gambar menjadi bitmap kemudian diaplikasikan pada
    // image_view dalam expandedView
    // implementasinya menggunakan runBlocking coroutine (mirip konsep async-await pada Javascript) yang
    // mana akan memblocking thread hingga block coroutine siap dijalankan
    fun applyImageUrl(
        imageUrl: String?,
        view: RemoteViews
    ) = runBlocking {
        // tampung url gambar
        val url = URL(imageUrl)

        // panggil withContext(Dispatchers.IO) untuk membuat blok yang akan dijalankan pada IO Dispatcher
        withContext(Dispatchers.IO) {
            // gunakan try catch untuk menangkap exception jika terjadi error
            try {
                // konversi url menjadi bitmap dengan openStream kemudian decodeStream
                val input = url.openStream()
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                null
            }
            // jika berhasil, hasil return bitmap akan diset menjadi gambar dari image_view
        }?.let { bitmap ->
            view.setImageViewBitmap(R.id.image_view_expanded, bitmap)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    // pada method onDestroy() dipanggil ketika activity dihancurkan
    override fun onDestroy() {
        // panggil method onDestoy() dari class parent
        super.onDestroy()

        // cancel notification dengan notification id 1 (dalam kasus ini yang menghandle video)
        // untuk menghapus notification
        notificationManager?.cancel(1)
    }
}