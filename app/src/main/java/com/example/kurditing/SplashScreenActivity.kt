package com.example.kurditing

import android.annotation.TargetApi
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kurditing.home.HomeActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

// inisialisasi sound pool dengan nilai null
var sp : SoundPool? = null
// inisialisasi soundId dengan nilai nol
var soundID  : Int = 0
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // lakukan delay pada splash screen selama 3 detik sebelum memasuki halaman sign in dengan
        // memanfaatkan handler
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }

    // method onStart akan dijalankan ketika activity dimulai
    override fun onStart() {
        // panggil method onStart dari class parent
        super.onStart()
        // lakukan pengecekan versi sdk, untuk membuat sound pool dengan metode yang sesuai, dimana
        // versi lama menggunakan pembentukan objek melalui constructor, sedangkan versi baru
        // menggunakan Builder()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            createNewSoundPool()
        else
            createOldSoundPool()

        // setOnLoadCompleteListener pada sound pool, dimana akan mendeteksi ketika sound pool
        // berhasil diload
        sp?.setOnLoadCompleteListener{soundPool, id, status ->
            // jika status bernilai bukan nol (gagal), maka tampilkan Toast yang menunjukkan gagal
            // melakukan load
            if(status != 0) {
                Toast.makeText(this,"Gagal Load",Toast.LENGTH_SHORT).show()
            }
            // jika status bernilai 0, maka tampilkan Toast dengan pesan load sukses, kemudian play
            // audio dengan nilai id berasal dari parameter id
            else {
                Toast.makeText(this,"Load Sukses",Toast.LENGTH_SHORT)
                        .show()
                sp?.play(id,.99f,.99f,1,0,.99f)
            }

        }

        // lakukan load file audio R.raw.kurditing pada sound pool, dimana akan mereturn nilai
        // sound id yang akan ditampung pada variabel soundID
        soundID = sp?.load(this, R.raw.kurditing,1) ?: 0
    }

    // anotasi TargetApi di bawah untuk menargetkan versi LOLLIPOP
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun createNewSoundPool() {
        // lakukan pembuatan SoundPool menggunakan Builder dengan melakukan setMaxStreams sebesar 15
        sp = SoundPool.Builder()
                .setMaxStreams(15)
                .build()
    }

    // anotasi Suppress di bawah untuk mengabaikan pesan deprecation
    @Suppress("DEPRECATION")
    private fun createOldSoundPool() {
        // lakukan pembuatan SoundPool menggunakan constructor dengan 3 buah argumen, yaitu maksimum
        // jumlah stream yang dapat dijalankan secara simultan, tipe audio stream, kualitas
        // sample-rate converter
        sp = SoundPool(15, AudioManager.STREAM_MUSIC,0)
    }

    // method onStop dijalankan ketika activity dalam status stop
    override fun onStop() {
        // panggil method onStop dari class parent
        super.onStop()
        // lakukan release resource soun pool untuk menghemat memori
        sp?.release()
        // kemudian
        sp = null
    }
}