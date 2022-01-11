package com.example.kurditing

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_video_playing.*

class VideoPlayingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_playing)

//        val videoPath = "android.resource://" + packageName + "/" + R.raw.video
        val videoPath = "https://firebasestorage.googleapis.com/v0/b/kurditing.appspot.com/o/videos%2FRecord%20of%20Ragnarok%20-%20Teaser%20Trailer%20(ENG%20subbed).mp4?alt=media&token=20389dec-36d4-438a-b687-2da9ff44c1b5"
//        val uri: Uri = Uri.parse(videoPath)
        val uri: Uri = Uri.parse(videoPath)
        video_view.setVideoURI(uri)
        val mediaController = MediaController(this)
        video_view.setMediaController(mediaController)
        mediaController.setAnchorView(video_view)
        video_view.start()
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
}