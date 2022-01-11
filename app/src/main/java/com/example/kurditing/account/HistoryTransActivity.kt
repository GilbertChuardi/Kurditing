package com.example.kurditing.account

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kurditing.R
import kotlinx.android.synthetic.main.activity_history_trans.*
import kotlinx.android.synthetic.main.activity_terms.iv_back
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class HistoryTransActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_trans)

        btn_simpan_trans.setOnClickListener{
            writeFileInternal();
        }
        
        btn_lihat_trans.setOnClickListener{
            readFileInternla();
        }
    }

    private fun readFileInternla() {
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
}
