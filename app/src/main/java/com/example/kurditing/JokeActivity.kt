package com.example.kurditing

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.WebViewFragment
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_joke.*

class JokeActivity : AppCompatActivity() {
    // JobId untuk menandakan id dari job yang akan dijalankan
    private var JobId = 20
    // variabel randomJoke untuk menampung joke yang akan ditampilkan
    private var randomJoke: String? = "Klik Lagi untuk memulai melihat lelucon bapak-bapak!"

    // jokeReceiver yang mengimplementasikan BroadcastReceiver berfungsi untuk menerima broadcast
    // yang dikirim oleh MyJobService
    private var jokeReceiver = object : BroadcastReceiver() {
        // method onReceive dijalankan ketika receiver menerima intent broadcast
        override fun onReceive(context: Context?, intent: Intent?) {
            // tampung joke yang dikirim melalui intent kemudian assign pada variabel randomJoke
            var joke = intent?.getStringExtra("JOKE")
            randomJoke = joke
        }
    }

    // Anotasi @RequiresApi(Build.VERSION_CODES.LOLLIPOP) diperlukan karena projek aplikasi menggunakan
    // minimum API level 16, namun method onCreate mengandung kode program yang memerlukan API level 21
    // yaitu startJob
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joke)

        // panggil startJob untuk menjadwalkan job
        startJob()
        // set text dari tv_joke menjadi randomJoke
        tv_joke.text = randomJoke

        // hanya untuk set visibility dari icon like
        btn_suka.setOnClickListener {
            iv_like.visibility = VISIBLE
        }

        // ketika button Lagi diklik panggil startJob() kemudian set visibility dari icon like menjadi
        // invisible serta set text dari tv_joke
        btn_lagi.setOnClickListener {
            startJob()
            iv_like.visibility = INVISIBLE
            tv_joke.text = randomJoke
        }

        // buat sebuah intent filter dengan action "ACTION_JOKE"
        var filterJoke = IntentFilter("ACTION_JOKE")
        // lakukan register receiver
        registerReceiver(jokeReceiver, filterJoke)
    }

    // Anotasi @RequiresApi(Build.VERSION_CODES.LOLLIPOP) diperlukan karena projek aplikasi menggunakan
    // minimum API level 16, namun fungsi startJob() mengandung kode program yang memerlukan API level 21
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startJob() {
        // buat sebuah serviceComponent melalui context dan class
        var serviceComponent = ComponentName(this, MyJobService::class.java)
        // buat sebuah JobInfo builder dengan argumen JobId beserta serviceComponent dimana JobInfo
        // yang dibuat memerlukan network, tidak mewajibkan device dalam keadaan idle maupun charging
        val mJobInfo = JobInfo.Builder(JobId, serviceComponent)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
        // buat sebuah JobScheduler melalui getSystemService
        var JobJoke = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        // kemudian jadwalkan job yang akan dieksekusi
        JobJoke.schedule(mJobInfo.build())
    }

    // method onDestroy dijalankan ketika activity dihancurkan
    override fun onDestroy() {
        // panggil method onDestroy dari class parent
        super.onDestroy()
        // lakukan unregister receiver
        unregisterReceiver(jokeReceiver)
    }
}