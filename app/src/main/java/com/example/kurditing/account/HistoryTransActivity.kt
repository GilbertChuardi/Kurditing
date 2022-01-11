package com.example.kurditing.account

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.kurditing.R
import kotlinx.android.synthetic.main.activity_history_trans.*
import kotlinx.android.synthetic.main.activity_terms.iv_back
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class HistoryTransActivity : AppCompatActivity() {
    // Anotasi @RequiresApi(Build.VERSION_CODES.M) menunjukkan bahwa fungsi hanya akan dipanggil pada
    // API level 23 atau diatasnya
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_trans)

        // ketika btn_simpan_trans diklik, cek apabila external storage dapat diakses, maka panggil
        // fungsi writeFileExternal()
        btn_simpan_trans.setOnClickListener{
            if (isExternalStorageReadable()) {
                writeFileExternal()
            }
        }

        // ketika btn_lihat_trans diklik, cek apabila external storage dapat diakses, maka panggil
        // fungsi readFileExternal()
        btn_lihat_trans.setOnClickListener{
            if (isExternalStorageReadable()) {
                readFileExternal()
            }
        }
    }

    // Anotasi @RequiresApi(Build.VERSION_CODES.KITKAT) menunjukkan bahwa fungsi hanya akan dipanggil pada
    // API level 19 atau diatasnya
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun writeFileExternal() {
        // buat instance dari File dengan argumen pemanggilan metode getExternalFilesDir() untuk membentuk file
        // di penyimpanan eksternal yang bersifat privat (pada kasus ini di dalam DIRECTORY_DOCUMENTS)
        var myDir = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.toURI())
        // jika direktori belum ada, maka buat direktori
        if(!myDir.exists()){
            myDir.mkdir()
        }

        // lakukan penulisan pada file dengan nama "riwayat_transaksi_eksternal". Pada contoh kasus ini,
        // digunakan dummy data yang dihardcode secara langsung
        File(myDir,"riwayat_transaksi_eksternal").apply {
            writeText("16 Mei 2021\n" +
                    "Social Media Marketing : Rp. 125.000\n\n" +
                    "08 Mei 2021\n" +
                    "SEO sampai bisa : Rp. 299.000\n\n" +
                    "11 April 2021\n" +
                    "Content Marketing : Rp. 125.000\n\n" +
                    "01 April 2021\n" +
                    "Funnel Mastery : Rp. 2.999.000\n\n" +
                    "12 Februari 2021\n" +
                    "Email Marketing : Rp. 125.000")
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun readFileExternal() {
        // buat instance dari File dengan argumen pemanggilan metode getExternalFilesDir() untuk mengakses file
        // di penyimpanan eksternal yang bersifat privat (pada kasus ini di dalam DIRECTORY_DOCUMENTS)
        var myDir = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.toURI())
        // set nilai tv_riwayat_trans menjadi string kosong
        tv_riwayat_trans.text = ""
        // inisialisasi variabel readFile dengan string kosong untuk menampung teks yang dibaca
        var readFile = ""
        // tampung instance file kedalam variabel untuk dibaca ukurannya
        var myFile = File(myDir, "riwayat_transaksi_eksternal")

        // tuliskan jumlah space yang digunakan menggunakan method length dari objek File (hasil return berupa bytes)
        // ke dalam variabel hasil
        readFile += "Jumlah space yang digunakan: ${myFile.length()} bytes\n\n"

        // untuk setiap baris dari myFile, tampung pada variabel readFile dengan menambahkan karakter
        // newline ("\n") untuk setiap barisnya
        myFile.forEachLine(Charsets.UTF_8) {
            readFile += it + "\n"
        }
        // lakukan assignment nilai readFile ke dalam tv_riwayat_trans
        tv_riwayat_trans.text = readFile
    }

    // Fungsi untuk memeriksa apakah status storage external dapat diakses atau tidak
    @RequiresApi(Build.VERSION_CODES.M)
    fun isExternalStorageReadable(): Boolean{
        // cek jika permission untuk READ_EXTERNAL_STORAGE sudah disetujui atau belum, jika belum maka
        // panggil fungsi requestPermissions (meminta izin akses) dengan argumen berupa array of string dari permissions beserta
        // request code
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                123)
        }

        // tampung state dari external storage
        var state = Environment.getExternalStorageState()
        // jika status bernilai MEDIA_MOUNTED atau MEDIA_MOUNTED_READ_ONLY, return nilai true, jika tidak
        // maka return false
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            return true
        }
        return false
    }

    // fungsi callback dari requestPermissions yang menerima parameter requestCode, permissions,
    // dan grantResults
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // cek berdasarkan requestCode, apabila bernilai 123, cek apakah array grantResults tidak kosong
        // dan grantResult bernilai disetujui. Jika ya, maka tampilkan Toast dengan pesan Izin diberikan
        when (requestCode) {
            123 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(this, "Izin Diberikan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Internal ----------------------------------------------------------------------------------------------

    private fun readFileInternal() {
        tv_riwayat_trans.text="";
        try{
            var input = openFileInput("riwayat_transaksi.txt").apply {
                bufferedReader().useLines {
                    for(text in it.toList()){
                        tv_riwayat_trans.setText("${tv_riwayat_trans.text}\n$text")
                    }
                }
            }
        }
        catch (e: FileNotFoundException){
            Toast.makeText(this, "File Not Found",Toast.LENGTH_SHORT).show()
        }catch (e: IOException){
            Toast.makeText(this, "File can't be read",Toast.LENGTH_SHORT).show()
        }
        var myFile = File(this.filesDir, "riwayat_transaksi.txt")
    }

    private fun writeFileInternal() {
        var output = openFileOutput("riwayat_transaksi.txt", Context.MODE_PRIVATE).apply {
            write(("16 Mei 2021\n" +
                    "Social Media Marketing : Rp. 125.000\n\n" +
                    "08 Mei 2021\n" +
                    "SEO sampai bisa : Rp. 299.000\n\n" +
                    "11 April 2021\n" +
                    "Content Marketing : Rp. 125.000\n\n" +
                    "01 April 2021\n" +
                    "Funnel Mastery : Rp. 2.999.000\n\n" +
                    "12 Februari 2021\n" +
                    "Email Marketing : Rp. 125.000").toByteArray())
            close()
        }
        var myFile = File(this.filesDir, "riwayat_transaksi.txt")
        Log.w("OK",myFile.absolutePath)
        Toast.makeText(this, "File Save", Toast.LENGTH_SHORT).show()
//        myFile.writeText("Hallo")
    }

    fun backHome(view: View) {
        finish();
    }
}
