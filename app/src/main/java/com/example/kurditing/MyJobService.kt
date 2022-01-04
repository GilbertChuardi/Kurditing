package com.example.kurditing

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

// Anotasi @RequiresApi(Build.VERSION_CODES.LOLLIPOP) diperlukan karena projek aplikasi menggunakan
// minimum API level 16, namun class MyJobService mengandung kode program yang memerlukan API level 21
// yaitu jobFinished
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
// class MyJobService menginherit dari class JobService
class MyJobService: JobService() {
    // method onStartJob dijalankan ketika job mulai dieksekusi
    override fun onStartJob(params: JobParameters?): Boolean {
        // Log hanya untuk keperluan debug
        Log.e("TEST", "start job")
        // panggil fungsi getRandomJokes dengan argumen params
        getRandomJokes(params)
        // return true agar job berjalan pada background
        return true
    }

    // method onStopJob dijalankan ketika eksekusi job dihentikan
    override fun onStopJob(params: JobParameters?): Boolean {
        // Log hanya untuk keperluan debug
        Log.e("TEST", "stop job")
        // return true menandakan bahwa job dapat dijadwalkan ulang ketika gagal
        return true
    }

    // fungsi getRandomJokes untuk melakukan fetching data lelucon dari API
    private fun getRandomJokes(params: JobParameters?) {
        // buat sebuah objek client menggunakan AsyncHttpClient
        var client = AsyncHttpClient()
        // tambahkan header dengan key "Accept" dan value "application/json", ini merupakan
        // ketentuan yang terdapat pada dokumentasi API yang akan digunakan
        client.addHeader("Accept", "application/json")
        // buat variabel url untuk menampung endpoint dari API
        var url = "https://icanhazdadjoke.com/"
        // charset untuk keperluan transformasi format data
        val charset = Charsets.UTF_8
        // buat sebuah objek handler yang menginherit dari AsyncHttpResponseHandler
        var handler = object : AsyncHttpResponseHandler() {
            // method onSuccess dijalankan ketika request berhasil menerima response
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                // transform responseBody dengan tipe data ByteArray menjadi string
                var result = responseBody!!.toString(charset)
                // buat JSONObject dari JSON string, kemudian ambil value dari field dengan key "joke"
                var joke = JSONObject(result).getString("joke")
                // buat sebuah intent dengan action "ACTION_JOKE" untuk mengirim string joke melalui extra
                // pada intent
                val intent = Intent("ACTION_JOKE")
                intent.putExtra("JOKE", joke)
                // kirim intent menggunakan sendBroadcast()
                sendBroadcast(intent)
                // Log hanya untuk keperluan debug
                Log.w("TEST", result)
                Log.w("TEST", joke)

                // panggil JobFinished untuk memberitahukan JobScheduler bahwa job sudah selesai
                jobFinished(params,false)
            }

            // method onFailure dijalankan ketika request gagal menerima response
            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                // panggil JobFinished untuk memberitahukan JobScheduler bahwa job sudah selesai (gagal)
                jobFinished(params, true)
                // Log hanya untuk keperluan debug
                Log.w("TEST", "failed")
            }
        }
        // panggil method get untuk melakukan HTTP GET request
        client.get(url, handler)
    }
}