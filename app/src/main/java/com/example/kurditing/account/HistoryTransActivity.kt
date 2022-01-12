package com.example.kurditing.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.kurditing.R
import kotlinx.android.synthetic.main.activity_history_trans.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.String
import kotlin.random.Random

class HistoryTransActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_trans)

        var db= Room.databaseBuilder(
            this,
            MyDBRoomHelper::class.java,
            "kurditing.db"
        ).build()

        btn_simpan_trans.setOnClickListener{
            // menjalankan fungsi writeFileInternal saat tombol btn_simpan_trans diklik
//            writeFileInternal();
            doAsync {
                var historyTmp = UserName(123)
                historyTmp.name = "Wilson Angga"
                db.usernameDAO().insertAll(historyTmp)
                uiThread {
                    Log.w("Hasil DB", "berhasil")
                }
            }
        }

        btn_lihat_trans.setOnClickListener{
            // menjalankan fungsi readFileInternal saat tombol btn_lihat_trans diklik
//            readFileInternal();
            var hasil = ""
            doAsync {
                db.historyTransactionDAO().getAllData().forEach{
                    hasil += it.created_at.toString() + "\n" + it.course_name.toString() + "\nIDR " + it.price.toString() + "\n\n"
                }
                tv_riwayat_trans.setText(hasil)
                uiThread {
                    Log.w("Hasil DB", hasil)
                }
            }
        }
    }

    // fungsi untuk membaca file
    private fun readFileInternal() {
        // membersihkan text pada tv_riwayat_trans
        tv_riwayat_trans.text="";
        try{
            // deklarasi variabel untuk membuka file dengan nama riwayat_transaksi.txt
            var input = openFileInput("riwayat_transaksi.txt").apply {
                // membaca file perbaris
                bufferedReader().useLines {
                    // perulangan text dalam baris
                    for(text in it.toList()){
                        // memasukkan text kedalam tv_riwayat trans
                        tv_riwayat_trans.setText("${tv_riwayat_trans.text}\n$text")
                    }
                }
            }
        }
        catch (e: FileNotFoundException){
            // menampilkan toast jika file tidak ditemukan
            Toast.makeText(this, "File Not Found", Toast.LENGTH_SHORT).show()
        }catch (e: IOException){
            // menampilkan toast jika file tidak bisa dibaca
            Toast.makeText(this, "File can't be read", Toast.LENGTH_SHORT).show()
        }
        // deklarasi variable untuk mengupdate file riwayat_transaksi.txt
//        var myFile = File(this.filesDir, "riwayat_transaksi.txt")
    }

    // fungsi untuk menulis dan menyimpan file
    private fun writeFileInternal() {
        // deklarasi variabel untuk menulis dan menyimpan file dengan nama riwaya_transaksi.txt
        var output = openFileOutput("riwayat_transaksi.txt", Context.MODE_PRIVATE).apply {
            // menuliskan data kedalam file
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
            // menutup file
            close()
        }
        // deklarasi variable untuk mengupdate file riwayat_transaksi.txt
//        var myFile = File(this.filesDir, "riwayat_transaksi.txt")
//        Log.w("OK",myFile.absolutePath)
        // cek besar file
//        var length = myFile.length()
//        length = length
//        Toast.makeText(this,"File Path : " + myFile.path + ", File size : " + length + " Bytes",Toast.LENGTH_SHORT).show()
        // menmapilkan toast bahwa file telah tersimpan
//        Toast.makeText(this, "File Save", Toast.LENGTH_SHORT).show()
    }
}
