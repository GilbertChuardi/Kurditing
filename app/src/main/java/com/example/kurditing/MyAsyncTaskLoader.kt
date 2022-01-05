package com.example.kurditing

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader

// class MyAsyncTaskLoader yang mengextends class AsyncTaskLoader yang mereturn data string
class MyAsyncTaskLoader(context: Context) : AsyncTaskLoader<String>(context) {
    // method loadInBackground akan dipanggil ketika MyAsyncTaskLoader dijalankan oleh LoaderManager
    override fun loadInBackground(): String? {
        // perulangan hanya untuk simulasi saja, dengan menggunakan Thread.sleep
        for (i in 0..100) {
            try {
                Thread.sleep(50)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        // data yang direturn berupa string, dalam kasus ini masih berupa data statis yang dihardcode
        // namun dimungkinkan untuk menggunakan data dinamis
        return "Selamat Anda mendapatkan diskon 50% kursus FB Ads Pro"
    }

    // method onStartLoading dipanggil sesaat sebelum loadInBackground() dijalankan
    override fun onStartLoading() {
        // panggil method onStartLoading dari class parent
        super.onStartLoading()
        // memaksa loading secara asynchronous
        forceLoad()
    }
}