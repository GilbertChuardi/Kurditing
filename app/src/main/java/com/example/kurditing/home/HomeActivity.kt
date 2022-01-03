package com.example.kurditing.home

import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kurditing.MyNetworkReceiver
import com.example.kurditing.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*

// implement interface NetworkReceiverListener pada HomeActivity
class HomeActivity : AppCompatActivity(), MyNetworkReceiver.NetworkReceiverListener {
    // buat sebuah snackbar untuk memberi pesan jika tidak terkoneksi / sudah terkoneksi kembali
    private var snackbar: Snackbar? = null
    // variabel firstLaunch hanya untuk track apakah aplikasi pertama kali dijalankan atau tidak, bertujuan
    // untuk mencegah munculnya snackbar yang menandakan koneksi kembali ketika awal aplikasi dijalankan
    private var firstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // laukan inisialisasi bottom navigation view bersasarkan view pada file xml (disini saya namakan id-nya bnv)
        val bnv = bnv
        // pembuatan navController sebagai pengatur navigasi (disini R.id.fragment pada file xml merupakan
        // sebuah NavHostFragment
        val navController = findNavController(R.id.fragment)

        // lakukan set up NavController berdasarkan variabel navController yang telah dibuat untuk
        // sinkronisasi antara bottomNavigationView dengan navController
        bnv.setupWithNavController(navController)

        var cek = intent.getStringExtra("course")
        if(cek == "course"){
            navController.navigate(R.id.courseFragment)
        }

        // assign nilai this pada companion object networkReceiverListener yang sebelumnya dibuat pada
        // networkReceiverListener
        MyNetworkReceiver.networkReceiverListener = this
        // lakukan register receiver MyNetworkReceiver dengan IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(MyNetworkReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    // override method onNetworkConnectionChanged dengan parameter isConnected
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // panggil fungsi showNetworkMessage dengan argumen isConnected
        showNetworkMessage(isConnected)
    }

//    override fun onResume() {
//        super.onResume()
//        MyNetworkReceiver.networkReceiverListener = this
//    }

    // fungsi showNetworkMessage untuk menampilkan pesan Snackbar
    fun showNetworkMessage(isConnected: Boolean) {
        // jika tidak terkoneksi
        if (!isConnected) {
            // cek jika firstLaunch, maka set jadi false, kemudian tampilkan snackbar yang menandakan sedang offline
            if (firstLaunch) firstLaunch = false
            snackbar = Snackbar.make(
                findViewById(R.id.root),
                "You are offline",
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar?.view?.setBackgroundColor(Color.parseColor("#f44336"))
            snackbar?.show()
        } else {
            // cek jika bukan firstLaunch, maka tampilkan snackbar yang menandakan koneksi telah terhubung kembali
            if (firstLaunch) return
            snackbar?.dismiss()
            snackbar = Snackbar.make(
                findViewById(R.id.root),
                "We are back!",
                Snackbar.LENGTH_SHORT
            )
            snackbar?.view?.setBackgroundColor(Color.parseColor("#4caf50"))
            snackbar?.show()
        }
    }

//    fun dialog(value: Boolean) {
//        if (value) {
//            tv_check_connection.setText("We are back !!!")
//            tv_check_connection.setBackgroundColor(Color.GREEN)
//            tv_check_connection.setTextColor(Color.WHITE)
//            val handler = Handler()
//            val delayrunnable = Runnable { tv_check_connection.setVisibility(View.GONE) }
//            handler.postDelayed(delayrunnable, 3000)
//        } else {
//            tv_check_connection.setVisibility(View.VISIBLE)
//            tv_check_connection.setText("Could not Connect to internet")
//            tv_check_connection.setBackgroundColor(Color.RED)
//            tv_check_connection.setTextColor(Color.WHITE)
//        }
//    }
}